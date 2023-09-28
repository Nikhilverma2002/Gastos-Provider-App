// Generated by view binder compiler. Do not edit!
package com.cu.gastosmerchant1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cu.gastosmerchant1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BottomsheetDialogBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView cardPay;

  @NonNull
  public final LinearLayout sheet;

  @NonNull
  public final ImageView upiPay;

  private BottomsheetDialogBinding(@NonNull LinearLayout rootView, @NonNull ImageView cardPay,
      @NonNull LinearLayout sheet, @NonNull ImageView upiPay) {
    this.rootView = rootView;
    this.cardPay = cardPay;
    this.sheet = sheet;
    this.upiPay = upiPay;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BottomsheetDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BottomsheetDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.bottomsheet_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BottomsheetDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.card_pay;
      ImageView cardPay = ViewBindings.findChildViewById(rootView, id);
      if (cardPay == null) {
        break missingId;
      }

      LinearLayout sheet = (LinearLayout) rootView;

      id = R.id.upi_pay;
      ImageView upiPay = ViewBindings.findChildViewById(rootView, id);
      if (upiPay == null) {
        break missingId;
      }

      return new BottomsheetDialogBinding((LinearLayout) rootView, cardPay, sheet, upiPay);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}