package by.md.gornak.homework.fragment;


import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.DesktopAppAdapter;
import by.md.gornak.homework.adapter.TouchHelper;
import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.ContactGenerator;

import static android.app.Activity.RESULT_OK;

public class DesktopFragment extends AppFragment implements DesktopAppAdapter.OnDragListener {

    protected static final int CONTACT_PICKER_RESULT = 1;

    protected DesktopAppAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected ItemTouchHelper mItemTouchHelper;
    protected int currentPosition = -1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_desktop, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        setupRecyclerView();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Bundle extras = data.getExtras();
                    Uri uriContact = data.getData();
                    ApplicationDB newContact = ContactGenerator.createContact(getActivity(), uriContact);
                    newContact.setPosition(currentPosition);
                    appsDesktop.set(currentPosition, newContact);
                    AppFragment.apps.put(newContact.getAppPackage(), newContact);
                    mAdapter.notifyDataSetChanged();

                    YandexMetrica.reportEvent(getString(R.string.yandex_add_contact));
                    break;
            }

        }
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void addApp(String packageName) {
        for (int i = 0; i < appsDesktop.size(); i++) {
            ApplicationDB app = appsDesktop.get(i);
            if (app == null) {
                app = apps.get(packageName);
                app.setDesktop(true);
                app.setPosition(i);
                appsDesktop.set(i, app);
                Toast.makeText(getContext(), R.string.desktop_done, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public int removeApp(String packageName) {
        int pos = -1;
        for (ApplicationDB app : appsDesktop) {
            if (app != null && app.getAppPackage().equals(packageName)) {
                pos = app.getPosition();
                appsDesktop.set(pos, null);
                break;
            }
        }
        return pos;
    }

    @Override
    protected void showDialog(final String packageName, final int position) {
        if(packageName != null) {
            super.showDialog(packageName, position);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.app_dialog_title);

        String[] actions = getResources().getStringArray(R.array.empty_desktop_dialog_action);

        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openContacts(position);
                        break;
                    default:
                        break;
                }

            }
        });

        builder.create().show();
    }

    private void openContacts(int position) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    0);
        } else {
            currentPosition = position;
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
        }
    }

    protected void openInfoApp(String packageName) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + packageName));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    protected int getFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            return apps.get(packageName).getFrequency();
        } else {
            return 0;
        }
    }

    protected void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new DesktopAppAdapter(getContext(), appsDesktop, appListener, this);

        ItemTouchHelper.Callback callback =
                new TouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void removeFromDesktop(String packageName) {
        super.removeFromDesktop(packageName);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void changePosition(List<ApplicationDB> changes) {
        for(ApplicationDB app : changes) {
            apps.put(app.getAppPackage(), app);
        }
    }


}
