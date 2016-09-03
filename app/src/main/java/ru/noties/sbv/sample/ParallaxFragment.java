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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.noties.sbv.ScrollingBackgroundView;

public class ParallaxFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        return inflater.inflate(R.layout.fragment_parallax, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        final int step = getResources().getDimensionPixelSize(R.dimen.space_explorer_step);

        {
            final ScrollingBackgroundView clouds = findView(view, R.id.fragment_parallax_clouds);
            setUp(clouds, step, 1.F);
        }

        {
            final ScrollingBackgroundView birds = findView(view, R.id.fragment_parallax_bird);
            setUp(birds, step, 1.5F);
        }
    }

    private void setUp(final ScrollingBackgroundView view, final int step, final float speed) {
        view.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                final ValueAnimator animator = ValueAnimator.ofFloat(.0F, 1.F);
                animator.setEvaluator(new FloatEvaluator());
                animator.setDuration(400L);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        view.scrollBy(
                                (int) (step * speed + .5F),
                                0
                        );
                    }
                });
                animator.start();
            }
        });
    }
}
