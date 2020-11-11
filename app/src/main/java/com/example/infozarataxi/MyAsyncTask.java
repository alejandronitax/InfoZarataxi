package com.example.infozarataxi;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    private static Dialog pbarProgreso;
    private Context context;

    public MyAsyncTask(Context contexto) {

        this.context = contexto;
        pbarProgreso = new Dialog(contexto);
        pbarProgreso.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pbarProgreso.setContentView(R.layout.prograss_bar_dialog);
        ProgressBar  mProgressBar1 = pbarProgreso.findViewById(R.id.progress_bar);
        TextView progressText =  pbarProgreso.findViewById(R.id.progress_text);
        progressText.setText("Guardando");
        progressText.setVisibility(View.VISIBLE);
        mProgressBar1.setVisibility(View.VISIBLE);
        mProgressBar1.setIndeterminate(true);

    }

    public static void hideProgress() {

        if (pbarProgreso != null) {
            pbarProgreso.dismiss();
            pbarProgreso = null;
        }
    }

    @Override
    protected void onPreExecute() {
        pbarProgreso.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        for (int i = 1; i <= 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            publishProgress(i * 10);
            if (isCancelled())
                break;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(pbarProgreso != null ){
            pbarProgreso.dismiss();
        }

        if (result) {
            Toast.makeText(context, "Tarea finalizada true!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Tarea finalizada false!", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onCancelled() {
        pbarProgreso.dismiss();
    }

}