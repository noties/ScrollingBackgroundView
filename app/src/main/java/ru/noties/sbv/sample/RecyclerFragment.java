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
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.noties.sbv.ScrollingBackgroundView;

public class RecyclerFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {
        return inflater.inflate(R.layout.fragment_recycler, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        setDrawable(R.drawable.tile_chat);

        final RecyclerView recyclerView = findView(view, R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            final ScrollingBackgroundView scrollingBackgroundView = scrollingBackgroundView();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollingBackgroundView.scrollBy(dx, dy);
            }
        });
        recyclerView.setAdapter(new Adapter(getContext()));
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private static final int TYPE_LEFT  = 0;
        private static final int TYPE_RIGHT = 1;

        private final LayoutInflater mInflater;
        private final List<Item> mItems;
        private final Random mRandom;
        private final TextGenerator mTextGenerator;

        Adapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
            this.mItems = new ArrayList<>();
            this.mRandom = new Random();
            this.mTextGenerator = new TextGenerator(mRandom);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            final int layout;
            if (TYPE_LEFT == viewType) {
                layout = R.layout.adapter_recycler_left_item;
            } else if (TYPE_RIGHT == viewType) {
                layout = R.layout.adapter_recycler_right_item;
            } else {
                throw new IllegalStateException("Unknown type: " + viewType);
            }
            return new Holder(mInflater.inflate(layout, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.textView.setText(getItem(position).text);
        }

        private Item getItem(int position) {
            final Item item;
            // check if item exists
            if (position < mItems.size()) {
                item = mItems.get(position);
            } else {
                // generate new one
                item = createNewItem();
                mItems.add(item);
            }
            return item;
        }

        private Item createNewItem() {
            final int type = mRandom.nextBoolean() ? TYPE_RIGHT : TYPE_LEFT;
            final String text = mTextGenerator.generate();
            return new Item(type, text);
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        static class Holder extends RecyclerView.ViewHolder {

            final TextView textView;

            public Holder(View itemView) {
                super(itemView);

                textView = findView(itemView, R.id.recycler_text);
            }
        }

        static class Item {

            private final int type;
            private final String text;

            Item(int type, String text) {
                this.type = type;
                this.text = text;
            }
        }

        static class TextGenerator {

            private static final String[] EMOJIS;
            static {
                EMOJIS = new String[] {
                        new String(Character.toChars(0x1F601)),
                        new String(Character.toChars(0x1F602)),
                        new String(Character.toChars(0x1F603)),
                        new String(Character.toChars(0x1F604)),
                        new String(Character.toChars(0x1F605)),
                        new String(Character.toChars(0x1F606)),
                };
            }

            private final Random mRandom;

            TextGenerator(Random random) {
                mRandom = random;
            }

            String generate() {
                
                final int words = mRandom.nextInt(19);

                final StringBuilder builder = new StringBuilder();

                if (words > 0) {

                    int length;
                    char[] chars;

                    for (int i = 0; i < words; i++) {
                        if (i > 0) {
                            builder.append(' ');
                        }
                        length = mRandom.nextInt(9) + 1;
                        chars = new char[length];
                        for (int c = 0; c < length; c++) {
                            chars[c] = (char) (97 + mRandom.nextInt(26)); // 97 = 'a', 26 is the length of the alphabet
                        }
                        builder.append(chars);
                    }
                }

                // add emoji
                if (words == 0 || mRandom.nextBoolean()) {
                    final int length = mRandom.nextInt(4) + 1;
                    for (int i = 0; i < length; i++) {
                        builder.append(EMOJIS[mRandom.nextInt(EMOJIS.length)]);
                    }
                }
                
                return builder.toString();
            }
        }
    }
}
