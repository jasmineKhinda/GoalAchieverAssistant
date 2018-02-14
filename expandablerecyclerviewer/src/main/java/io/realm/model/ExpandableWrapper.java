package io.realm.model;

import android.support.annotation.NonNull;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Wrapper used to link metadata with a list item.
 *
 * @param <P> Parent list item
 * @param <C> Child list item
 */
public class ExpandableWrapper<P extends Parent<C>, C extends Child> implements RealmModel {

    private P parent;
    private C child;
    private boolean wrappedParent;
    private boolean expanded;

    private RealmList<ExpandableWrapper<P, C>> wrappedChildList;

    /**
     * Constructor to wrap a parent object of type {@link P}.
     *
     * @param parent The parent object to wrap
     */
    public ExpandableWrapper(@NonNull P parent) {
        this.parent = parent;
        wrappedParent = true;
        expanded = false;

        wrappedChildList = generateChildItemList(parent);
    }

    /**
     * Constructor to wrap a child object of type {@link C}
     *
     * @param child The child object to wrap
     */
    public ExpandableWrapper(@NonNull C child) {
        this.child = child;
        wrappedParent = false;
        expanded = false;
    }

    public P getParent() {
        return parent;
    }

    public void setParent(@NonNull P parent) {
        this.parent = parent;
        wrappedChildList = generateChildItemList(parent);
    }

    public C getChild() {
        return child;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isParent() {
        return wrappedParent;
    }

    /**
     * @return The initial expanded state of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public boolean isParentInitiallyExpanded() {
        if (!wrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return parent.isExpanded();
    }

    /**
     * @return The list of children of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public RealmList<ExpandableWrapper<P, C>> getWrappedChildList() {
        if (!wrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return wrappedChildList;
    }

    private RealmList<ExpandableWrapper<P, C>> generateChildItemList(P parentListItem) {
        RealmList<ExpandableWrapper<P, C>> childItemList = new RealmList<>();

        //all added below
        Log.d("GOALS", "in the expandable recycler adapter sorting the subtask list");
        RealmList<C> childList = parentListItem.getChildList();
        RealmResults<C> childResults = childList.where().findAllSortedAsync(
                new String[] {"dueDateNotEmpty", "dueDate"},
                new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

        RealmList<C> childListOrdered = new RealmList<C>();

        //TODO: order the subtask list in expandable view
        for (C child : childResults) {
            childItemList.add(new ExpandableWrapper<P, C>(child));
        }

//        //TODO: order the subtask list in expandable view
//        for (C child : parentListItem.getChildList()) {
//            childItemList.add(new ExpandableWrapper<P, C>(child));
//        }

        return childItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ExpandableWrapper<?, ?> that = (ExpandableWrapper<?, ?>) o;

        if (parent != null ? !parent.equals(that.parent) : that.parent != null)
            return false;
        return child != null ? child.equals(that.child) : that.child == null;

    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
