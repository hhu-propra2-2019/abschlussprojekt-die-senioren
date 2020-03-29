package mops.gruppen1.domain;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Representing a module within a course.
 */
@Data
public class Module {

    private List<User> users;
    private ModuleName modulename;
    private UUID id;
    private ModuleSemester moduleSemester;
}
