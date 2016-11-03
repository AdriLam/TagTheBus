package Bdd;

import android.graphics.Bitmap;

/**
 * Created by adrien on 28/10/16.
 */

public class Photo {

    public long id_photo;
    public long id_station;
    public Bitmap photo;
    public String date_photo;
    public String nom_photo;

    public long getId() {
        return id_photo;
    }

    public void setId(long id_photo) {
        this.id_photo = id_photo;
    }

    public long getIdStation() {
        return id_station;
    }

    public void setIdStation(long id_station) {
        this.id_station = id_station;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDate() {
        return date_photo;
    }

    public void setDate(String date_photo) {
        this.date_photo = date_photo;
    }

    public String getNom() {
        return nom_photo;
    }

    public void setNom(String nom_photo) {
        this.nom_photo = nom_photo;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id_photo=" + id_photo +
                ", id_station=" + id_station +
                ", nom_photo=" + nom_photo +
                ", date_photo='" + date_photo + '\'' +
                '}';
    }
}
