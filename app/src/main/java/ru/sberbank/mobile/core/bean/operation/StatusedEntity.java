package ru.sberbank.mobile.core.bean.operation;

import com.google.common.base.Objects;

import org.simpleframework.xml.Transient;

/**
 * Базовая сущность-ответ от модели, обладающая статусом.
 *
 * @author QuickNick
 */
public abstract class StatusedEntity {

    @Transient
    private boolean mHandled;

    public StatusedEntity() {
        mHandled = false;
    }

    public final boolean isHandled() {
        return mHandled;
    }

    public final void setHandled(boolean handled) {
        mHandled = handled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusedEntity)) {
            return false;
        }
        StatusedEntity that = (StatusedEntity) o;
        return mHandled == that.mHandled;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mHandled);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("mHandled", mHandled)
                .toString();
    }

    public abstract boolean isSuccess();
}
