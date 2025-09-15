package com.spring.DocManager.repository;

import static nu.studer.sample.tables.Groups.GROUPS;
import com.spring.DocManager.model.Group;
import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.GroupsRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final DSLContext dsl;

    public List<Group> findAll() {
        return dsl.selectFrom(GROUPS)
                .orderBy(GROUPS.NAME.asc())
                .fetch()
                .map(this::mapToGroup);
    }

    public Optional<Group> findById(Long id) {
        return Optional.ofNullable(
                dsl.selectFrom(GROUPS)
                        .where(GROUPS.ID.eq(id))
                        .fetchOne()
        ).map(this::mapToGroup);
    }

    public Group save(Group group) {
        if (group.getId() == null) {
            GroupsRecord savedRecord = dsl.insertInto(GROUPS)
                    .set(setGroupFields(group))
                    .returning()
                    .fetchOptional()
                    .orElseThrow(() -> new IllegalStateException("Не удалось сохранить группу"));
            return mapToGroup(savedRecord);
        } else {
            dsl.update(GROUPS)
                    .set(setGroupFields(group))
                    .where(GROUPS.ID.eq(group.getId()))
                    .execute();

            return group;
        }
    }

    public void deleteById(Long id) {
        dsl.deleteFrom(GROUPS)
                .where(GROUPS.ID.eq(id))
                .execute();
    }

    private Group mapToGroup(GroupsRecord record) {
        Group group = new Group();
        group.setId(record.getId());
        group.setName(record.getName());
        group.setColor(record.getColor());
        return group;
    }

    private Map<Field<?>, Object> setGroupFields(Group group) {
        Map<Field<?>, Object> fields = new HashMap<>();
        fields.put(GROUPS.NAME, group.getName());
        fields.put(GROUPS.COLOR, group.getColor());
        return fields;
    }

}
