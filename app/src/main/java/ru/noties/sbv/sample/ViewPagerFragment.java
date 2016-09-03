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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.noties.ccf.CCFAnimator;
import ru.noties.sbv.ScrollingBackgroundView;

public class ViewPagerFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        return inflater.inflate(R.layout.fragment_pager, parent, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle sis) {
        super.onViewCreated(view, sis);

        final ViewPager viewPager = findView(view, R.id.view_pager);
        viewPager.setAdapter(new Adapter(getContext()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            final Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.tile_pager).mutate();
            final ScrollingBackgroundView scrollingBackgroundView = scrollingBackgroundView();
            final CCFAnimator ccfAnimator = CCFAnimator.rgb(new int[] {
                    0xFFef9a9a,
                    0xFFf48fb1,
                    0xFFce93d8,
                    0xFFb39ddb,
                    0xFF9fa8da,
                    0xFF90caf9,
                    0xFF81d4fa,
                    0xFF80deea,
                    0xFF80cbc4,
                    0xFFa5d6a7
            });
            {
                setColor(drawable, ccfAnimator.getColor(.0F));
                scrollingBackgroundView.setDrawable(drawable);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // total count
                final int count = viewPager.getAdapter().getCount();
                final float step = (float) (position + 1) / count;
                final float ratio = step + (positionOffset / count);

                if (positionOffsetPixels != 0) {
                    scrollingBackgroundView.scrollTo(positionOffsetPixels / 2, 0);
                }

                setColor(drawable, ccfAnimator.getColor(ratio));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void setColor(Drawable drawable, int color) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            }
        });
    }

    private static class Adapter extends PagerAdapter {

        private final LayoutInflater mInflater;

        Adapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final TextView textView = (TextView) mInflater.inflate(R.layout.adapter_view_pager_item, container, false);
            textView.setText(String.format("Page: %1$d", position));
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
