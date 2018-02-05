package by.md.gornak.homework.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import by.md.gornak.homework.R;


public class HelloFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hello, container, false);
        setAvatar(rootView);
        return rootView;
    }

    private void setAvatar(View rootView) {
        ImageView avatar = rootView.findViewById(R.id.avatar);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        src = Bitmap.createBitmap(
                src,
                (int) (src.getWidth() * 0.15),
                (int) (src.getWidth() * 0.15),
                (int) (src.getWidth() * 0.7),
                (int) (src.getWidth() * 0.7)
        );
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCircular(true);
        avatar.setImageDrawable(dr);
    }
}
