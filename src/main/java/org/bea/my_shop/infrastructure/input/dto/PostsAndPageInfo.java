package org.bea.my_shop.infrastructure.input.dto;

import java.util.List;

public record PostsAndPageInfo(List<?> posts, PageOfPostsResponse pageOfPosts) {
}
