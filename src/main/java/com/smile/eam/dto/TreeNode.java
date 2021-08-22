package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {
    private int id;
    private int parentId;
    private String name;
    private String title;
    private int value;
    private int flag;
    private List<TreeNode> children= new ArrayList<>();
}
