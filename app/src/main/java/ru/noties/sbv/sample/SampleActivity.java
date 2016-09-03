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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;

import ru.noties.debug.Debug;

public class SampleActivity extends AppCompatActivity {

    private static final String ARG_FRAGMENT = "arg.Fragment";

    public static Intent makeIntent(Context context, String fragmentClassName) {
        final Intent intent = new Intent(context, SampleActivity.class);
        intent.putExtra(ARG_FRAGMENT, fragmentClassName);
        return intent;
    }

    @Override
    public void onCreate(Bundle sis) {
        super.onCreate(sis);

        final Fragment fragment = fragment();
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(Window.ID_ANDROID_CONTENT, fragment)
                    .commitAllowingStateLoss();
        } else {
            finish();
        }
    }

    private Fragment fragment() {
        final String name = getIntent().getStringExtra(ARG_FRAGMENT);
        if (!TextUtils.isEmpty(name)) {
            try {
                final Class<?> cl = Class.forName(name);
                return (Fragment) cl.newInstance();
            } catch (Throwable t) {
                Debug.e(t);
            }
        }
        return null;
    }
}
