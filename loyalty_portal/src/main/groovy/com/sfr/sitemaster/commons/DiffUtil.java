package com.sfr.sitemaster.commons;

import com.sfr.sitemaster.entities.ChangeDTO;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import de.danielbechler.diff.path.NodePath;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.diff.selector.RootElementSelector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by piotr on 17/11/15.
 */
public final class DiffUtil {

    private DiffUtil() {
    }

    public static <T> List<ChangeDTO> compare(final T obj1, final T obj2) {
        return new DiffUtil().doCompare(obj1, obj2);
    }


    private <T> List<ChangeDTO> doCompare(final T before, final T after) {
        final ObjectDifferBuilder diffBuilder = ObjectDifferBuilder.startBuilding();
        //TODO: @piotr should this be configurable? How to ignore fields, annotation maybe?
        diffBuilder.inclusion().exclude().propertyName("created").propertyName("timestamp").propertyName("modelSchema").propertyName("id");
        final DiffNode diff = diffBuilder.build().compare(after, before);
        final List<ChangeDTO> changes = new ArrayList<>();
        diff.visit((node, visit) -> {
            if (node.getPropertyName() != null && node.hasChanges()) {
                final ChangeDTO change = getChange(node, visit, before, after);
                if (change != null) {
                    changes.add(change);
                }
            }
        });
        return changes;
    }

    private ChangeDTO getChange(final DiffNode node, final Visit visit, final Object beforeObj, final Object afterObj) {
        String before = null;
        String after = null;
        if (node.hasChildren()) {
            if (node.getValueType() == LocalDate.class || node.getValueType() == LocalDateTime.class) {
                before = node.canonicalGet(beforeObj) == null ? null : node.canonicalGet(beforeObj).toString();
                after = node.canonicalGet(afterObj) == null ? null : node.canonicalGet(afterObj).toString();
            } else {
                return null;
            }
        } else {
            before = node.canonicalGet(beforeObj) == null ? null : node.canonicalGet(beforeObj).toString();
            after = node.canonicalGet(afterObj) == null ? null : node.canonicalGet(afterObj).toString();
        }
        visit.dontGoDeeper();
        final ChangeDTO change = new ChangeDTO();
        final List path = getPath(node, afterObj);
        change.setPath(path);
        change.setBefore(before);
        change.setAfter(after);
        change.setState(node.getState().toString().toLowerCase());
        return change;
    }

    private List<String> getPath(final DiffNode node, final Object afterObj) {
        if (node.getPath().isChildOf(NodePath.with("openingInfo", "temporarilyClosed"))) {
            final int index = ((List) node.getParentNode().getParentNode().canonicalGet(afterObj)).indexOf(node.getParentNode().canonicalGet(afterObj));
            return node.getPath().getElementSelectors().stream().filter(it -> it.getClass() != RootElementSelector.class).map(it -> {
                if (it.getClass() == CollectionItemElementSelector.class) {
                    return "{" + index + "}";
                } else {
                    return it.toHumanReadableString();
                }
            }).map(Object::toString).collect(Collectors.toList());
        }
        return node.getPath().getElementSelectors().stream().filter(it -> it.getClass() != RootElementSelector.class).map(ElementSelector::toString).collect(Collectors.toList());
    }
}
