/*
 *   Copyright 2016 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package ru.noties.sbv.sample;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import ru.noties.sbv.ScrollingBackgroundView;

public class SpaceExplorerFragment extends BaseFragment {

    private int mVerticalStep;
    private int mHorizontalStep;

    private float mSpeed = 1.F;

    private ValueAnimator mValueAnimator;
    private View mShipView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        return inflater.inflate(R.layout.fragment_space_explorer, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        setDrawable(R.drawable.tile_space);

        final Resources r = getResources();
        final int step = r.getDimensionPixelSize(R.dimen.space_explorer_step);

        // horizontal
        {
            final NavigationGroup group = new NavigationGroup(
                    view.findViewById(R.id.fragment_space_explorer_direction_left),
                    view.findViewById(R.id.fragment_space_explorer_direction_right),
                    step,
                    new NavigationGroup.Callbacks() {
                        @Override
                        public void onNewValue(int value) {
                            mHorizontalStep = value;
                            animate();
                        }
                    }
            );
            group.setup();
        }

        // vertical
        {
            final NavigationGroup group = new NavigationGroup(
                    view.findViewById(R.id.fragment_space_explorer_direction_top),
                    view.findViewById(R.id.fragment_space_explorer_direction_bottom),
                    step,
                    new NavigationGroup.Callbacks() {
                        @Override
                        public void onNewValue(int value) {
                            mVerticalStep = value;
                            animate();
                        }
                    }
            );
            group.setup();
        }

        // speed
        {

            final Speed speed = new Speed();
            final TextView speedTextView = findView(view, R.id.fragment_space_explorer_speed);
            speedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSpeed = speed.next();
                    speedTextView.setText(r.getString(R.string.space_explorer_speed, mSpeed));
                }
            });
            speedTextView.callOnClick();
        }

        mShipView = findView(view, R.id.fragment_space_explorer_ship);
    }

    private void animate() {

        if (mValueAnimator == null
                || !mValueAnimator.isRunning()) {

            final ScrollingBackgroundView backgroundView = scrollingBackgroundView();

            mValueAnimator = ValueAnimator.ofFloat(.0F, 1.F);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setEvaluator(new FloatEvaluator());
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setDuration(400L);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final int x = (int) (mHorizontalStep * mSpeed + .5F);
                    final int y = (int) (mVerticalStep * mSpeed + .5F);
                    backgroundView.scrollBy(x, y);
                    mShipView.setRotation(shipRotation(x, y));
                }
            });
            mValueAnimator.start();
        }
    }

    private static float shipRotation(int x, int y) {
        final double out = 90.D + (Math.atan2(y, x) * (180.D / Math.PI));
        return (float) out;
    }

    private static class NavigationGroup {

        interface Callbacks {
            void onNewValue(int value);
        }

        private final View negative;
        private final View positive;
        private final int value;
        private final Callbacks callbacks;

        NavigationGroup(View negative, View positive, int value, Callbacks callbacks) {
            this.negative = negative;
            this.positive = positive;
            this.value = value;
            this.callbacks = callbacks;
        }

        void setup() {
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ok, we detect 2 cases:
                    // if this view is already active -> then deactivate
                    // if the opposite view active -> deactivate it
                    // otherwise -> activate this
                    if (v.isActivated()) {
                        v.setActivated(false);
                        update(0);
                    } else {
                        positive.setActivated(false);
                        v.setActivated(true);
                        update(-value);
                    }
                }
            });
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isActivated()) {
                        v.setActivated(false);
                        update(0);
                    } else {
                        negative.setActivated(false);
                        v.setActivated(true);
                        update(value);
                    }
                }
            });
        }

        private void update(int value) {
            callbacks.onNewValue(value);
        }
    }

    private static class Speed {

        private static final float[] VALUES = {
                .5F, 1.F, 2.F, 3.F
        };

        private int mIndex = 0;

        Speed() {}

        float next() {
            return VALUES[++mIndex % VALUES.length];
        }
    }
}
