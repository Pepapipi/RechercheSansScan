package com.example.recycler;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.JsonReader;


import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Récupère à partir de données json où un code barres avec l'API d'OpenFoodFacts un objet Produit
 * avec son code barres, son nom, ses marques ainsi que ses instructions pour le tri de ses
 * emballages.
 *
 * Pour créer un objet produit il faut soit utiliser le constructeur avec des données json ayant au
 * minimum comme forme :
 * <pre>
 *     {@code
 *     {
 *         "code": "123456789",
 *         "product":{
 *             "brands": "Une Marque, Une autre Marque, ...",
 *             "product_name": "Nom du produit",
 *             "packaging_text_fr": "Instruction de recyclage"
 *         },
 *         "status": 1,
 *         "status_verbose": "product found"
 *     }}
 * </pre>
 *
 * Ou invoquer la methode statique {@link #getProductFromBarCode} avec le code barres du produit
 * recherché. <strong>Cette methode ne marche seulement si les permissions internet ont été
 * accordées</strong>. Pour cela il suffit d'ajouter au fichier <strong>AndroidManifest.xml</strong>
 * la ligne :
 * {@code <uses-permission android:name="android.permission.INTERNET"/>}.
 */
public class Produit {
    private String code;
    private String nom;
    private String marques;
    private String texteEmbalage;
    private String urlImage;
    private Drawable image;
    public String[] emball;

    /**
     * Construit un produit à partir de données json.
     * @param json Données json sous forme de chaîne de caractères.
     * @throws IOException Si les données json fournies ne sont pas valides.
     * @throws Resources.NotFoundException Si le produit n'a pas été trouvé.
     */
    public Produit(String json) throws IOException, Resources.NotFoundException {
        try (JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name) {
                    case "code":
                        this.code = reader.nextString();
                        break;
                    case "product":
                        reader.beginObject();
                        while (reader.hasNext()){
                            name = reader.nextName();
                            switch (name) {
                                case "brands":
                                    this.marques = reader.nextString();
                                    break;
                                case "product_name":
                                    this.nom = reader.nextString();
                                    break;
                                case "packaging_text_fr":
                                    this.texteEmbalage = reader.nextString();
                                    break;
                                case "image_url":
                                    this.urlImage = reader.nextString();
                                    break;
                                case "packaging":
                                    emball = reader.nextString().split(",");
                                    break;

                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();
                        break;
                    case "status":
                        int status = reader.nextInt();
                        if (status == 0) {
                            name = reader.nextName();
                            throw new Resources.NotFoundException(reader.nextString());
                        } else {
                            reader.nextName();
                            reader.nextString();
                        }
                        break;
                }

            }
        }
    }

    public Produit(){}

    /**
     *
     * @return Le code barres du produit.
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @return Le nom du produit.
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @return La liste des marques du produit.
     */
    public String getMarque() {
        return marques;
    }

    /**
     *
     * @return Les instructions vis-à-vis des embalages.
     */
    public String getTexteEmballage() {
        return texteEmbalage;
    }


    /**
     *
     * @return L'image du produit.
     */
    public String getUrlImage() {
        return urlImage;
    }

    public Drawable getImage(){return  image;}

    public void loadImage() throws  Exception{
        image = new ImageProduit().execute(urlImage).get();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMarques(String marques) {
        this.marques = marques;
    }

    public void setTexteEmbalage(String texteEmbalage) {
        this.texteEmbalage = texteEmbalage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    /**
     * Créé et renvoit un Produit à partir d'un code barres. Seulement si les permissions internet
     * ont été accordées.
     *
     * @param barCode Code barres du produit sous forme de chaîne de caractères.
     * @return Le produit correspondant au code barres.
     * @throws IOException Si les données json fournies ne sont pas valides.
     * @throws Resources.NotFoundException Si le produit n'a pas été trouvé.
     * @throws CancellationException If the computation was cancelled.
     * @throws ExecutionException If the computation threw an exception.
     * @throws InterruptedException If the current thread was interrupted while waiting.
     */
    public static Produit getProductFromBarCode(String barCode) throws IOException, Resources.NotFoundException, CancellationException, ExecutionException, InterruptedException {
        return new Produit(new OpenFoodFactsAPI().execute("https://fr.openfoodfacts.org/api/v0/product/" + barCode + ".json").get());
    }


    /**
     * Créé et renvoit une liste de Produits à partir d'un String contenant des données Json.
     * @param json String contenant des données Json.
     * @return List de Produits.
     * @throws IOException Si les données json fournies ne sont pas valides.
     */
    @NonNull
    public static List<Produit> getProductsListFromJson(String json) throws IOException{

        List<Produit> produits = new ArrayList<>();

        try(JsonReader reader = new JsonReader(new StringReader(json))){
            reader.beginObject();
            String name;
            while (reader.hasNext()){
                name = reader.nextName();
                if (name.equals("products")){
                    reader.beginArray();
                    while (reader.hasNext()){
                        reader.beginObject();
                        Produit produit = new Produit();
                        while (reader.hasNext()){
                            name = reader.nextName();
                            switch (name){
                                case "code":
                                    produit.setCode(reader.nextString());
                                    break;
                                case "product_name":
                                    produit.setNom(reader.nextString());
                                    break;
                                case "brands":
                                    produit.setMarques(reader.nextString());
                                    break;
                                case "packaging_text_fr":
                                    produit.setTexteEmbalage(reader.nextString());
                                    break;
                                case "image_url":
                                    produit.setUrlImage(reader.nextString());
                                    break;
                                case "packaging":
                                    produit.emball = reader.nextString().split(",");
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        produits.add(produit);
                        reader.endObject();
                    }
                    reader.endArray();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }

        return produits;
    }

}