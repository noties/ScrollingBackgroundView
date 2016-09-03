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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.noties.sbv.ScrollingBackgroundView;

public abstract class BaseFragment extends Fragment {

    private ScrollingBackgroundView mScrollingBackgroundView;

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis);

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        mScrollingBackgroundView = findView(view, R.id.scrolling_background_view);
    }

    public void setDrawable(Drawable drawable) {
        mScrollingBackgroundView.setDrawable(drawable);
    }

    public void setDrawable(int drawable) {
        setDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    protected ScrollingBackgroundView scrollingBackgroundView() {
        return mScrollingBackgroundView;
    }

    protected static <V extends View> V findView(View view, int id) {
        //noinspection unchecked
        return (V) view.findViewById(id);
    }
}
