package mops.gruppen1.data.DAOs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GroupDAO {
    private String groupId;
    private String groupName;
    private String course;
    private String groupDescritption;

    public GroupDAO(String groupId, String groupName, String groupDescritption) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescritption = groupDescritption;
    }


}
