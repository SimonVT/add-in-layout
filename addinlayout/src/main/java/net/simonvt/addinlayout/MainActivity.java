/*
 * Copyright (C) 2014 Simon Vig Therkildsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.simonvt.addinlayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

  MyViewGroup mvg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mvg = new MyViewGroup(this);
    setContentView(mvg);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 1, 0, "Remove in layout");

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 1) {
      if (mvg.removeInLayout) {
        Toast.makeText(this, "No longer removing in layout", Toast.LENGTH_SHORT).show();
        mvg.removeInLayout = false;
      } else {
        Toast.makeText(this, "Removing textview in layout", Toast.LENGTH_SHORT).show();
        mvg.removeInLayout = true;
      }
      return true;
    }

    return false;
  }

  public static class MyViewGroup extends ViewGroup {

    View view;

    int count;

    boolean removeInLayout = true;

    public MyViewGroup(Context context) {
      super(context);
      init();
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      init();
    }

    private void init() {
      view = LayoutInflater.from(getContext()).inflate(R.layout.myview, this, false);
      addView(view);
      setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          count++;
          requestLayout();
        }
      });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
      if (removeInLayout) {
        removeViewInLayout(view);
      }

      invalidate();

      final String text = "Count: " + count;
      ((TextView) view.findViewById(R.id.one)).setText(text);
      ((TextView) view.findViewById(R.id.two)).setText(text);
      ((TextView) view.findViewById(R.id.three)).setText(text);
      ((TextView) view.findViewById(R.id.four)).setText(text);

      if (removeInLayout) {
        LayoutParams lp = view.getLayoutParams();
        addViewInLayout(view, 0, lp);
        final int wms = MeasureSpec.makeMeasureSpec(r - l, MeasureSpec.EXACTLY);
        final int hms = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        view.measure(wms, hms);
      }

      view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
          MeasureSpec.getSize(heightMeasureSpec));

      measureChild(view, widthMeasureSpec, heightMeasureSpec);
    }
  }
}
