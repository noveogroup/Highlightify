Highlightify
============
Highlightify is Android library that helps you highlight views when you press them.
No more useless selectors and duplicate images with slightly different brightness.

#FAQ
### There's a slight delay before highlight takes effect
ScrollLayout is to blame.
See https://developer.android.com/reference/android/widget/FrameLayout.html#shouldDelayChildPressedState() for more details
