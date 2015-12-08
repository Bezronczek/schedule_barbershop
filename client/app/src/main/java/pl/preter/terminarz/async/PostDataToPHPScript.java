package pl.preter.terminarz.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import pl.preter.terminarz.models.PHPRequestData;

/**
 * Created by Mort on 2014-07-12.
 */
public class PostDataToPHPScript extends AsyncTask<PHPRequestData, Void, JSONObject> {
    //TODO
    @Override
    protected JSONObject doInBackground(PHPRequestData... params) {
        Log.v(this.getClass().getSimpleName(), params.toString());
        return null;


    }

}
