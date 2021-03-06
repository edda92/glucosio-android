package org.glucosio.android.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.glucosio.android.R;
import org.glucosio.android.db.Reminder;
import org.glucosio.android.presenter.RemindersPresenter;
import org.glucosio.android.tools.AnimationTools;

import java.util.Calendar;

public class RemindersActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private FloatingActionButton addFab;
    private RemindersPresenter presenter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        presenter = new RemindersPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(2);
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_reminders_title));
        }

        addFab = (FloatingActionButton) findViewById(R.id.activity_reminders_fab_add);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Time Picker on FAB click
                boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(getApplicationContext());
                Calendar cal = presenter.getCalendar();

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        RemindersActivity.this,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        is24HourFormat);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        listView = (ListView) findViewById(R.id.activity_reminders_listview);
        listView.setEmptyView(findViewById(R.id.activity_reminders_listview_empty));
        listView.setAdapter(presenter.getAdapter());
        addFab.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationTools.startCircularReveal(addFab);
            }
        }, 600);
    }

    public void updateReminder(Reminder reminder) {
        presenter.updateReminder(reminder);
        presenter.saveReminders();
    }

    public void updateRemindersList() {
        listView.setAdapter(presenter.getAdapter());
        listView.invalidate();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        // Id is HOURS+MINUTES to avoid duplicates
        String concatenatedId = hourOfDay + "" + minute;
        // Metric is always glucose until I write support for other metrics so...
        // TODO: Add Reminders for other metrics
        // Also oneTime is always set to false until I implement one time alarms
        // TODO: Implement one time alarms
        presenter.addReminder(Long.parseLong(concatenatedId), cal.getTime(), "glucose", false, true);
    }

    public void showDuplicateError() {
        View parentLayout = findViewById(R.id.activity_reminders_root_view);
        Snackbar.make(parentLayout, R.string.activitiy_reminders_error_duplicate, Snackbar.LENGTH_SHORT).show();
    }

    public void showBottomSheetDialog(final long id) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.fragment_reminders_bottom_sheet, null);
        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteReminder(id);
                updateRemindersList();
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            presenter.saveReminders();
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
