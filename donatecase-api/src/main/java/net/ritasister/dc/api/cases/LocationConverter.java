package net.ritasister.dc.api.cases;

/**
 * Interface for converting Location objects to and from String representations,
 * as well as checking if two locations are effectively the same.
 */
public interface LocationConverter<L> {

    /**
     * Serializes a location object to its String representation.
     *
     * @param location the location object to serialize
     * @return the String representation of the location
     */
    String serialize(L location);

    /**
     * Deserializes a String representation back to a location object.
     *
     * @param location the String representation of the location
     * @return the deserialized location object
     */
    L deserialize(String location);

    /**
     * Checks if two location objects represent the same location.
     *
     * @param location1 the first location object
     * @param location2 the second location object
     * @return true if both locations are the same, false otherwise
     */
    boolean isHere(L location1, L location2);
}
