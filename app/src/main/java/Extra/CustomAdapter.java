package Extra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrien.tagthebus.R;

import java.util.ArrayList;
import java.util.List;

import Bdd.BddOpenHelper;
import Bdd.DataManager;
import Bdd.Photo;
import Bdd.PhotoData;

/**
 * Created by adrien on 31/10/16.
 */

// CustomAdapter.java
public class CustomAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "CustomAdapter";
    ArrayList<ListItem> myList = new ArrayList<ListItem>();
    Context context;

    /**
     * Constructor
     * @param context
     * @param myList
     */
    public CustomAdapter(Context context, ArrayList<ListItem> myList) {
        this.myList = myList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public ListItem getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myList.indexOf(getItem(position));
    }

    /**
     * Return the view of an element from the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder = null;

        // On inflate notre layout
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.list_station, parent, false);

            // nous plaçons dans notre MyViewHolder les vues de notre layout
            mViewHolder = new MyViewHolder();
            mViewHolder.textViewName = (TextView) convertView.findViewById(R.id.pic_date);
            mViewHolder.textViewAge = (TextView) convertView.findViewById(R.id.pic_name);
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageStation);

            // nous attribuons comme tag notre MyViewHolder à convertView
            convertView.setTag(mViewHolder);
        } else {
            // convertView n'est pas null, nous récupérons notre objet MyViewHolder
            // et évitons ainsi de devoir retrouver les vues à chaque appel de getView
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        // nous récupérons l'item de la liste demandé par getView
        ListItem listItem = (ListItem) getItem(position);

        // nous pouvons attribuer à nos vues les valeurs de l'élément de la liste
        mViewHolder.textViewName.setText(listItem.getName());
        mViewHolder.textViewAge.setText(String.valueOf(listItem.getDate()));
        mViewHolder.imageView.setImageBitmap(listItem.getImageId());

        // nous retournos la vue de l'item demandé
        return convertView;
    }

    /**
     * Execute action on ItemLongClick
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final ListItem thisItem = (ListItem) parent.getItemAtPosition(position);
        new AlertDialog.Builder(context).setTitle(R.string.supprimer).setMessage(R.string.supprimer_station_msg).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int whichButton) {

                String date = thisItem.getName();

                //Supression de la base de donnée
                PhotoData photoData = new PhotoData(context);

                try{
                    photoData.open(PhotoData.DELETE);
                    photoData.deletePhotoByDate(date);
                }catch (Exception e){
                    Log.e(TAG, "Error delet photo : "+e.toString());
                }finally {
                    photoData.close();
                }

                myList.remove(thisItem);
                notifyDataSetChanged();

            }
        }).setNegativeButton(android.R.string.no, null).show();

        return true;
    }

    /**
     * Execute action on ItemClick
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final Dialog dialog = new Dialog(context);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(d);
        dialog.setContentView(R.layout.custom_dialog);

        ImageView image = (ImageView) dialog.findViewById(R.id.image_dialog);

        final ListItem thisItem = (ListItem) parent.getItemAtPosition(position);
        Bitmap b = thisItem.getImageId();
        image.setImageBitmap(b);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // MyViewHolder va nous permettre de ne pas devoir rechercher
    // les vues à chaque appel de getView, nous gagnons ainsi en performance
    private class MyViewHolder {
        TextView textViewName, textViewAge;
        ImageView imageView;
    }

}