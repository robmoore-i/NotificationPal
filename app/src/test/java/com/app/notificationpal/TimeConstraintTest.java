package com.app.notificationpal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TimeConstraintTest {
    private Notifier mockNotifier;
    private Registry registry;

    @Before
    public void beforeEach() {
        mockNotifier = Mockito.mock(Notifier.class);
        registry = new Registry(mockNotifier);
    }

    @Test
    public void canApplyTimeConstraintForAfterSomeHour() {
        TimeConstraint timeConstraint = new TimeConstraint(TimeConstraint.Range.AFTER, TimeConstraint.Granularity.HOUR, 14);
        registry.registerNotification("It's after 2!", timeConstraint);
        timeConstraint.timeUpdate(2017, 8, 10, 14, 13, 0);
        verify(mockNotifier).createNotification("It's after 2!");
    }

    @Test
    public void doesntErroneouslyApplyTimeConstraintAfterSomeHour() {
        TimeConstraint timeConstraint = new TimeConstraint(TimeConstraint.Range.AFTER, TimeConstraint.Granularity.HOUR, 14);
        registry.registerNotification("It's after 2!", timeConstraint);
        timeConstraint.timeUpdate(2017, 8, 10, 12, 5, 0);
        verify(mockNotifier, never()).createNotification("It's after 2!");
    }

    @Test
    public void canApplyTimeConstraintForBeforeSomeMinute() {
        TimeConstraint timeConstraint = new TimeConstraint(TimeConstraint.Range.BEFORE, TimeConstraint.Granularity.MINUTE, 30);
        registry.registerNotification("It's the first half of the hour.", timeConstraint);
        timeConstraint.timeUpdate(2017, 8, 10, 15, 20, 0);
        verify(mockNotifier).createNotification("It's the first half of the hour.");
    }

    @Test
    public void canApplyTimeConstraintForAtSomeDay() {
        TimeConstraint timeConstraint = new TimeConstraint(TimeConstraint.Range.UPON, TimeConstraint.Granularity.DAY, 15);
        registry.registerNotification("It's the 15th of the month.", timeConstraint);
        timeConstraint.timeUpdate(2017, 10, 15, 15, 20, 0);
        verify(mockNotifier).createNotification("It's the 15th of the month.");
    }
}
