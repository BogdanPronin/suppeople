package com.github.bogdan.comparators;

import com.github.bogdan.model.Post;

import java.util.Comparator;

public class PostCategoryComparator implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
        return o1.getCategory().getName().compareTo(o2.getCategory().getName());
    }
}
