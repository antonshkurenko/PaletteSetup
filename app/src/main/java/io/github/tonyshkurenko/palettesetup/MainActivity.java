package io.github.tonyshkurenko.palettesetup;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import io.github.tonyshkurenko.palettesetup.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static int DEFAULT_TEXT_COLOR;

  ActivityMainBinding mBinding;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    Picasso.with(this).load(R.drawable.picture_girl).fit().centerCrop().into(mBinding.girl);
    Picasso.with(this)
        .load(R.drawable.picture_starry_night)
        .fit()
        .centerCrop()
        .into(mBinding.starryNight);
    Picasso.with(this).load(R.drawable.picture_scream).fit().centerCrop().into(mBinding.scream);

    mBinding.girl.setOnClickListener(this);
    mBinding.starryNight.setOnClickListener(this);
    mBinding.scream.setOnClickListener(this);

    {
      final int[] attrs = new int[] { android.R.attr.textColorSecondary };
      final TypedArray a = getTheme().obtainStyledAttributes(R.style.AppTheme, attrs);
      DEFAULT_TEXT_COLOR = a.getColor(0, Color.BLACK);
      a.recycle();
    }
  }

  @Override public void onClick(View view) {

    int resId;
    switch (view.getId()) {
      case R.id.girl:
        resId = R.drawable.picture_girl;
        break;
      case R.id.starry_night:
        resId = R.drawable.picture_starry_night;
        break;
      case R.id.scream:
        resId = R.drawable.picture_scream;
        break;
      default:
        return;
    }

    final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

    final long startTime = System.currentTimeMillis();
    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
      @Override public void onGenerated(Palette palette) {
        final long difference = System.currentTimeMillis() - startTime;
        setTitle(difference + " ms");
        processPalette(palette);
      }
    });
  }

  private void processPalette(Palette palette) {

    // vibrant
    final Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
    if (vibrantSwatch != null) {
      processSwatch(mBinding.vibrant, vibrantSwatch);
    } else {
      resetTextView(mBinding.vibrant);
    }

    // vibrant dark
    final Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();
    if (vibrantDarkSwatch != null) {
      processSwatch(mBinding.vibrantDark, vibrantDarkSwatch);
    } else {
      resetTextView(mBinding.vibrantDark);
    }

    // vibrant light
    final Palette.Swatch vibrantLightSwatch = palette.getLightVibrantSwatch();
    if (vibrantLightSwatch != null) {
      processSwatch(mBinding.vibrantLight, vibrantLightSwatch);
    } else {
      resetTextView(mBinding.vibrantLight);
    }

    // muted
    final Palette.Swatch mutedSwatch = palette.getMutedSwatch();
    if (mutedSwatch != null) {
      processSwatch(mBinding.muted, mutedSwatch);
    } else {
      resetTextView(mBinding.muted);
    }

    // muted dark
    final Palette.Swatch mutedDarkSwatch = palette.getDarkMutedSwatch();
    if (mutedDarkSwatch != null) {
      processSwatch(mBinding.mutedDark, mutedDarkSwatch);
    } else {
      resetTextView(mBinding.mutedDark);
    }

    // muted light
    final Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();
    if (mutedLightSwatch != null) {
      processSwatch(mBinding.mutedLight, mutedLightSwatch);
    } else {
      resetTextView(mBinding.mutedLight);
    }
  }

  private void processSwatch(TextView textView, Palette.Swatch vibrantSwatch) {
    textView.setBackgroundColor(vibrantSwatch.getRgb());
    textView.setTextColor(vibrantSwatch.getTitleTextColor());
  }

  private void resetTextView(TextView textView) {
    textView.setTextColor(DEFAULT_TEXT_COLOR);
    textView.setBackgroundColor(Color.TRANSPARENT);
  }
}
