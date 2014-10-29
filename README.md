Highlightify
============
Highlightify is Android library that helps you highlight views when you press them.
Usually you would create separate drawable with dark overlay or keep two almost identical images in your resources.
But no more, with Highlightify you can easily do in in runtime.

![alt tag](https://github.com/noveogroup/Highlightify/raw/master/HighlightifySample.gif)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Highlightify-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/696)

##Getting started

Add dependency:
```groovy
dependencies {
    compile 'com.noveogroup.android:highlightify:+'
}
```
Create Highlightify instance:
```java
// Using dark color and default set of targets
Highlightify highlightify = new Highlightify();

// Using Builder
Highlightify highlightify = new Highlightify.Builder()
    // With custom set of targets to highilight
    .addTargets(..)
    // With custom color
    .addTargets(Color.RED,..)
    // With custom ColorFilter
    .addTargets(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP))
    .build();
```
And highlight your views:

```java
// Highilight individual views
highlightify.highlight(view1, view2);

// Highlight all clickable views recursively
highlightify.highlightClickable(view);

// Highlight whole view hierarchy
highlightify.highlightWithChildren(view);
```

Thats it, now your views should be properly highlighted when you press them.

## How it works

Each drawable will be wrapped in instance of HighlightDrawable, which will apply color filter to initial one when approprite.<br>
**NOTE:** Highlight effect will be applied even if initial drawable already declare pressed_state.

In case of a text, color will be replaced with [ColorStateList](https://developer.android.com/reference/android/content/res/ColorStateList.html)<br>
**NOTE:** If text color already set as ColorStateList, it's pressed state will be overridden.

#FAQ
### There's a slight delay before highlight takes effect
ScrollLayout is to blame.
See https://developer.android.com/reference/android/widget/FrameLayout.html#shouldDelayChildPressedState() for more details

#Licence

    Copyright (c) 2014 Noveo Group
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    Except as contained in this notice, the name(s) of the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
