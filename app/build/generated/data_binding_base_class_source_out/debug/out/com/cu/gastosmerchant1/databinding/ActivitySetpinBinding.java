// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySetpinBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final EditText enterPin;

  @NonNull
  public final View layer3;

  @NonNull
  public final ImageView prev;

  @NonNull
  public final EditText renterPin;

  @NonNull
  public final Button setBtn;

  @NonNull
  public final TextView setYourPin;

  @NonNull
  public final TextView setYourPin2;

  private ActivitySetpinBinding(@NonNull RelativeLayout rootView, @NonNull EditText enterPin,
      @NonNull View layer3, @NonNull ImageView prev, @NonNull EditText renterPin,
      @NonNull Button setBtn, @NonNull TextView setYourPin, @NonNull TextView setYourPin2) {
    this.rootView = rootView;
    this.enterPin = enterPin;
    this.layer3 = layer3;
    this.prev = prev;
    this.renterPin = renterPin;
    this.setBtn = setBtn;
    this.setYourPin = setYourPin;
    this.setYourPin2 = setYourPin2;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySetpinBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySetpinBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_setpin, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySetpinBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.enterPin;
      EditText enterPin = ViewBindings.findChildViewById(rootView, id);
      if (enterPin == null) {
        break missingId;
      }

      id = R.id.layer_3;
      View layer3 = ViewBindings.findChildViewById(rootView, id);
      if (layer3 == null) {
        break missingId;
      }

      id = R.id.prev;
      ImageView prev = ViewBindings.findChildViewById(rootView, id);
      if (prev == null) {
        break missingId;
      }

      id = R.id.renterPin;
      EditText renterPin = ViewBindings.findChildViewById(rootView, id);
      if (renterPin == null) {
        break missingId;
      }

      id = R.id.setBtn;
      Button setBtn = ViewBindings.findChildViewById(rootView, id);
      if (setBtn == null) {
        break missingId;
      }

      id = R.id.set_your_pin;
      TextView setYourPin = ViewBindings.findChildViewById(rootView, id);
      if (setYourPin == null) {
        break missingId;
      }

      id = R.id.set_your_pin2;
      TextView setYourPin2 = ViewBindings.findChildViewById(rootView, id);
      if (setYourPin2 == null) {
        break missingId;
      }

      return new ActivitySetpinBinding((RelativeLayout) rootView, enterPin, layer3, prev, renterPin,
          setBtn, setYourPin, setYourPin2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}