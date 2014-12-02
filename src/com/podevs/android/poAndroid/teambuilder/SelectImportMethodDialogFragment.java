package com.podevs.android.poAndroid.teambuilder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.podevs.android.poAndroid.R;

/**
 * <p>Creates a dialog that proposes the use to import team from file.</p>
 * 
 * <p>If importing from file, will open a new ImportTeamFromFileDialogFragment,
 * which will call yourActivity.OnTeamImportedFromFileListener's interface function
 * onTeamImportedFromFile(FullPlayerInfo info).</p>
 * 
 * @author coyotte508
 *
 */
public class SelectImportMethodDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Import team...")
		.setSingleChoiceItems(new CharSequence[]{"Tutorial", "From file"}, -1, null)
		.setPositiveButton("Import", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int option = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				if (option == 0) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pokemon-online.eu/threads/importing-teams-on-po-android.19698/"));
					startActivity(browserIntent);
				} else if (option == 1) { // From File
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

					intent.setType("file/*.tp");
					intent = Intent.createChooser(intent, "File explorer");
					getActivity().startActivityForResult(intent, TeambuilderActivity.PICKFILE_RESULT_CODE);
				}
			}
		})
		.setNegativeButton(R.string.cancel, null);

		return builder.create();
	}
}
