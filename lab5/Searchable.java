public interface Searchable    {
    String getTitle(); //in interface --> no public or private needed in front
    default boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return false;
        String lowerkeyword = keyword.toLowerCase();
        return (getTitle() != null && getTitle().toLowerCase().contains(lowerkeyword));
    }
}