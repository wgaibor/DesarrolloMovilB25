package com.example.pokemonapp.model;

import java.util.List;

public class PokemonListResponse {
    private int count;
    private String next;
    private String previous;
    private List<PokemonListItem> results;

    public PokemonListResponse() {}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonListItem> getResults() {
        return results;
    }

    public void setResults(List<PokemonListItem> results) {
        this.results = results;
    }

    public static class PokemonListItem {
        private String name;
        private String url;

        public PokemonListItem() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
