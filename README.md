# Scrolling Background View
Endless background scrolling for Android


![](https://raw.githubusercontent.com/noties/ScrollingBackgroundView/master/gifs/gif_pager.gif)
![](https://raw.githubusercontent.com/noties/ScrollingBackgroundView/master/gifs/gif_recycler.gif)

Please note, that first GIF uses library: [CCF](https://github.com/noties/ColorCrossFade) to achive color-cross-fade effect, please refer to the sample project for more info.


| Version | Gradle |
| --- | --- |
| [![Maven Central](https://img.shields.io/maven-central/v/ru.noties/sbv.svg)](http://search.maven.org/#search\|ga\|1\|g%3A%22ru.noties%22%20AND%20a%3A%22sbv%22) | `compile 'ru.noties:sbv:x.x.x'` |

A simple Android View that can help to achive endless scrolling background effect. It takes one drawable and tiles it to fill to the width & height of the view.

```xml
<ru.noties.sbv.ScrollingBackgroundView
    android:id="@+id/scrolling_background_view"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:sbv_drawable="@drawable/tile_pager"
    app:sbv_scrollX="0dip"
    app:sbv_scrollY="0dip"/>
```

Custom attributes:
* `sbv_drawable` - the drawable object to be used
* `sbv_scrollX` - start `scroll x` value
* `sbv_scrollY` - start `scroll y` value

This view works with preview layout tools in Android Studio.

Next we need to redirect scroll events from our scrolling View to the ScrollingBackgroundView. The sample project contains some basic cases like RecyclerView & ViewPager.

```java
final ScrollingBackgroundView scrollingBackgroundView = findView(view, R.id.scrolling_background_view);

scrollingBackgroundView.setDrawable(/* drawable reference */);

scrollingBackgroundView.setOnSizeChangedListener(/* a listener to be notified about size changes of this view */);

// actual scrolling methods
scrollingBackgroundView.scrollBy(/* x & y values */);
scrollingBackgroundView.scrollTo(/* x & y values */);

scrollingBackgroundView.scrollX(); // use this method to retrieve current scroll x value
scrollingBackgroundView.scrollY(); // use this method to retrieve current scroll y value
```

```java
final RecyclerView recyclerView = findView(view, R.id.recycler_view);
recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        scrollingBackgroundView.scrollBy(dx, dy);
    }
});
```


## License

```
  Copyright 2016 Dimitry Ivanov (mail@dimitryivanov.ru)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```