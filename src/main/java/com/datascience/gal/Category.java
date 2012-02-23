package com.datascience.gal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datascience.gal.service.JSONUtils;
import com.datascience.gal.service.Service;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Category {
    public static final CategoryDeserializer deserializer = new CategoryDeserializer();

    private String name;

    // The prior probability for this category
    private double prior;

    // The misclassification cost when we classify an object of this category
    // into some other category. The HashMap key is the other category, and the
    // Double
    // is the cost.
    private Map<String, Double> misclassification_cost;

    private Category(String name, double prior,
            Map<String, Double> misclassification_cost) {
        this.name = name;
        this.prior = prior;
        this.misclassification_cost = misclassification_cost;
    }

    public Category(String name) {
        this.name = name;
        this.prior = -1.0;
        this.misclassification_cost = new HashMap<String, Double>();
    }

    public void setCost(String to, double cost) {
        misclassification_cost.put(to, cost);
    }

    public double getCost(String to) {
        return misclassification_cost.get(to);
    }

    /**
     * @return the prior
     */
    public double getPrior() {
        return prior;
    }

    /**
     * @param prior
     *            the prior to set
     */
    public void setPrior(double prior) {
        this.prior = prior;
    }

    /**
     * @param prior
     *            the prior to set
     */
    public boolean hasPrior() {
        if (this.prior != -1)
            return true;
        else
            return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Category))
            return false;
        Category other = (Category) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSONUtils.gson.toJson(this);
    }

    /**
     * TODO: also allow to accept misclassification costs - at the moment this
     * seems to come from the DS class
     * 
     * @author josh
     * 
     */
    public static class CategoryDeserializer implements
            JsonDeserializer<Category> {

        @Override
        public Category deserialize(JsonElement json, Type type,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject jobject = (JsonObject) json;
            String name = jobject.get("name").getAsString();
            double prior = jobject.get("prior").getAsDouble();
            Map<String, Double> misclassification_cost = JSONUtils.gson
                    .fromJson(jobject.get("misclassification_cost"),
                            JSONUtils.stringDoubleMapType);
            return new Category(name, prior, misclassification_cost);
        }

    }

}