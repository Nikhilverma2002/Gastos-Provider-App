// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SettingItemLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView imageSettingItem;

  @NonNull
  public final View line;

  @NonNull
  public final LinearLayout linearSettingItem;

  @NonNull
  public final TextView tittleSettingItem;

  private SettingItemLayoutBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView imageSettingItem, @NonNull View line,
      @NonNull LinearLayout linearSettingItem, @NonNull TextView tittleSettingItem) {
    this.rootView = rootView;
    this.imageSettingItem = imageSettingItem;
    this.line = line;
    this.linearSettingItem = linearSettingItem;
    this.tittleSettingItem = tittleSettingItem;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SettingItemLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SettingItemLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.setting_item_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SettingItemLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.image_setting_item;
      ImageView imageSettingItem = ViewBindings.findChildViewById(rootView, id);
      if (imageSettingItem == null) {
        break missingId;
      }

      id = R.id.line;
      View line = ViewBindings.findChildViewById(rootView, id);
      if (line == null) {
        break missingId;
      }

      id = R.id.linear_setting_item;
      LinearLayout linearSettingItem = ViewBindings.findChildViewById(rootView, id);
      if (linearSettingItem == null) {
        break missingId;
      }

      id = R.id.tittle_setting_item;
      TextView tittleSettingItem = ViewBindings.findChildViewById(rootView, id);
      if (tittleSettingItem == null) {
        break missingId;
      }

      return new SettingItemLayoutBinding((LinearLayout) rootView, imageSettingItem, line,
          linearSettingItem, tittleSettingItem);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}