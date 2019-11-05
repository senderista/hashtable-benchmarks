package set.int32;

/**
 * A 32-bit hash set.
 *
 * @author tdbaker
 */
public interface IntSet {

   /**
     * Query the number of elements in the table.
     *
     * @return the number of elements in the table
     */
    public int size();

    /**
     * Query the table for a value.
     *
     * @param value the 32-bit integer to query the table for
     * @return {@code true} if {@code value} is present in the table, {@code false} otherwise
     */
    public boolean contains(int value);

    /**
     * Add an element to the table.
     *
     * @param element the 32-bit integer to add to the table
     * @return {@code false} if {@code element} was already present in the table, {@code true} otherwise
     */
    public boolean add(int element);

    /**
     * Remove an element from the table.
     *
     * @param value the 32-bit integer to remove from the table
     * @return {@code false} if {@code value} was not present in the table, {@code true} otherwise
     */
    public boolean remove(int value);

    /**
     * Remove all elements from the table.
     */
    public void clear();

    /**
     * Return deep copy of the table.
     * @return the cloned table
     */
    public IntSet cloneSet() throws CloneNotSupportedException;

}
