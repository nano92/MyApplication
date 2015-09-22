/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer;

import com.google.android.exoplayer.SampleSource.SampleSourceReader;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.Loader.Loadable;

import android.net.Uri;
import android.os.SystemClock;

import java.io.IOException;
import java.util.Arrays;

/**
 * A {@link SampleSource} that loads the data at a given {@link Uri} as a single sample.
 */
public final class SingleSampleSource implements SampleSource, SampleSourceReader, Loader.Callback,
    Loadable {

  /**
   * The default minimum number of times to retry loading data prior to failing.
   */
  public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;

  /**
   * The initial size of the allocation used to hold the sample data.
   */
  private static final int INITIAL_SAMPLE_SIZE = 1;

  private final Uri uri;
  private final DataSource dataSource;
  private final MediaFormat format;
  private final TrackInfo trackInfo;
  private final int minLoadableRetryCount;

  private byte[] sampleData;
  private int sampleSize;
  private boolean pendingSample;

  private boolean loadingFinished;
  private Loader loader;
  private IOException currentLoadableException;
  private int currentLoadableExceptionCount;
  private long currentLoadableExceptionTimestamp;

  public SingleSampleSource(Uri uri, DataSource dataSource, MediaFormat format) {
    this(uri, dataSource, format, DEFAULT_MIN_LOADABLE_RETRY_COUNT);
  }

  public SingleSampleSource(Uri uri, DataSource dataSource, MediaFormat format,
      int minLoadableRetryCount) {
    this.uri = uri;
    this.dataSource = dataSource;
    this.format = format;
    this.minLoadableRetryCount = minLoadableRetryCount;
    trackInfo = new TrackInfo(format.mimeType, format.durationUs);
    sampleData = new byte[INITIAL_SAMPLE_SIZE];
  }

  @Override
  public SampleSourceReader register() {
    return this;
  }

  @Override
  public boolean prepare(long positionUs) {
    if (loader == null) {
      loader = new Loader("Loader:" + format.mimeType);
    }
    return true;
  }

  @Override
  public int getTrackCount() {
    return 1;
  }

  @Override
  public TrackInfo getTrackInfo(int track) {
    return trackInfo;
  }

  @Override
  public void enable(int track, long positionUs) {
    pendingSample = true;
    clearCurrentLoadableException();
    maybeStartLoading();
  }

  @Override
  public boolean continueBuffering(int track, long positionUs) {
    maybeStartLoading();
    return loadingFinished;
  }

  @Override
  public void maybeThrowError() throws IOException {
    if (currentLoadableException != null && currentLoadableExceptionCount > minLoadableRetryCount) {
      throw currentLoadableException;
    }
  }

  @Override
  public int readData(int track, long positionUs, MediaFormatHolder formatHolder,
      SampleHolder sampleHolder, boolean onlyReadDiscontinuity) {
    if (onlyReadDiscontinuity) {
      return NOTHING_READ;
    } else if (!pendingSample) {
      return END_OF_STREAM;
    } else if (!loadingFinished) {
      return NOTHING_READ;
    } else {
      sampleHolder.timeUs = 0;
      sampleHolder.size = sampleSize;
      sampleHolder.flags = C.SAMPLE_FLAG_SYNC;
      if (sampleHolder.data == null || sampleHolder.data.capacity() < sampleSize) {
        sampleHolder.replaceBuffer(sampleHolder.size);
      }
      sampleHolder.data.put(sampleData, 0, sampleSize);
      return SAMPLE_READ;
    }
  }

  @Override
  public void seekToUs(long positionUs) {
    pendingSample = true;
  }

  @Override
  public long getBufferedPositionUs() {
    return loadingFinished ? TrackRenderer.END_OF_TRACK_US : 0;
  }

  @Override
  public void disable(int track) {
    pendingSample = false;
  }

  @Override
  public void release() {
    if (loader != null) {
      loader.release();
      loader = null;
    }
  }

  // Private methods.

  private void maybeStartLoading() {
    if (loadingFinished || !pendingSample || loader.isLoading()) {
      return;
    }
    if (currentLoadableException != null) {
      long elapsedMillis = SystemClock.elapsedRealtime() - currentLoadableExceptionTimestamp;
      if (elapsedMillis < getRetryDelayMillis(currentLoadableExceptionCount)) {
        return;
      }
      currentLoadableException = null;
    }
    loader.startLoading(this, this);
  }

  private void clearCurrentLoadableException() {
    currentLoadableException = null;
    currentLoadableExceptionCount = 0;
  }

  private long getRetryDelayMillis(long errorCount) {
    return Math.min((errorCount - 1) * 1000, 5000);
  }

  // Loader.Callback implementation.

  @Override
  public void onLoadCompleted(Loadable loadable) {
    loadingFinished = true;
    clearCurrentLoadableException();
  }

  @Override
  public void onLoadCanceled(Loadable loadable) {
    // Never happens.
  }

  @Override
  public void onLoadError(Loadable loadable, IOException e) {
    currentLoadableException = e;
    currentLoadableExceptionCount++;
    currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
    maybeStartLoading();
  }

  // Loadable implementation.

  @Override
  public void cancelLoad() {
    // Never happens.
  }

  @Override
  public boolean isLoadCanceled() {
    return false;
  }

  @Override
  public void load() throws IOException, InterruptedException {
    // We always load from the beginning, so reset the sampleSize to 0.
    sampleSize = 0;
    try {
      // Create and open the input.
      dataSource.open(new DataSpec(uri));
      // Load the sample data.
      int result = 0;
      while (result != C.RESULT_END_OF_INPUT) {
        sampleSize += result;
        if (sampleSize == sampleData.length) {
          sampleData = Arrays.copyOf(sampleData, sampleData.length * 2);
        }
        result = dataSource.read(sampleData, sampleSize, sampleData.length - sampleSize);
      }
    } finally {
      dataSource.close();
    }
  }

}
