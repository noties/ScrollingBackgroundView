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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ru.noties.debug.Debug;
import ru.noties.debug.out.AndroidLogDebugOutput;

public class MainActivity extends AppCompatActivity {

    static {
        Debug.init(new AndroidLogDebugOutput(true));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Adapter adapter = new Adapter(this, new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String fragmentName) {
                final Intent intent = SampleActivity.makeIntent(MainActivity.this, fragmentName);
                startActivity(intent);
            }
        });
        adapter.setItems(items());

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private static List<Item> items() {
        return Arrays.asList(
                new Item("Space Explorer", SpaceExplorerFragment.class.getName()),
                new Item("Parallax", ParallaxFragment.class.getName()),
                new Item("Recycler View", RecyclerFragment.class.getName()),
                new Item("View Pager", ViewPagerFragment.class.getName())
        );
    }

    private static class Item {

        final String name;
        final String fragmentClassName;

        Item(String name, String fragmentClassName) {
            this.name = name;
            this.fragmentClassName = fragmentClassName;
        }
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        interface OnItemClickListener {
            void onItemClick(String fragmentName);
        }

        private final LayoutInflater mInflater;
        private final OnItemClickListener mOnItemClickListener;

        private List<Item> mItems;

        Adapter(Context context, OnItemClickListener onItemClickListener) {
            this.mInflater = LayoutInflater.from(context);
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setItems(List<Item> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(mInflater.inflate(R.layout.adapter_main_item, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Item item = mItems.get(position);
            holder.text.setText(item.name);
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(item.fragmentClassName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems != null ? mItems.size() : 0;
        }

        static class Holder extends RecyclerView.ViewHolder {

            final TextView text;

            public Holder(View itemView) {
                super(itemView);

                text = (TextView) itemView.findViewById(R.id.adapter_main_text);
            }
        }
    }
}
