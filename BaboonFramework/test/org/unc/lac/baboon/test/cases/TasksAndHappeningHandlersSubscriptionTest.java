package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Test;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.main.BaboonConfig;
import org.unc.lac.baboon.task.AbstractTask;
import org.unc.lac.baboon.task.HappeningHandlerObject;
import org.unc.lac.baboon.task.TaskObject;
import org.unc.lac.baboon.test.utils.tasks.MockController;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;

public class TasksAndHappeningHandlersSubscriptionTest {
    private final String topicsPath02 = "test/org/unc/lac/baboon/test/resources/topics02.json";
    private final String topicsPath03 = "test/org/unc/lac/baboon/test/resources/topics03.json";
    private final String[] topicNamesDefined = { "topic1", "topic2", "topic3", "topic4" };

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link HappeningHandler}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the
     * {@link HappeningHandler} annotated method to a {@link Topic}</li>
     * <li>Then the {@link HappeningHandlerObject} subscriptions Map should
     * contain a {@link HappeningHandlerObject} with the object instance and the
     * method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerObject} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingAnExistingHappeningHandlerToAnExistingTopicShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
            Map<AbstractTask, Topic> subscriptionsMap = baboonConfig.getSubscriptionsUnmodifiableMap();
            HappeningHandlerObject testHHO = new HappeningHandlerObject(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            assertEquals(1, subscriptionsMap.size());
            assertTrue(subscriptionsMap.keySet().contains(testHHO));
            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testHHO).getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the {@link Task}
     * annotated method to a {@link Topic}</li>
     * <li>Then the {@link TaskObject} subscriptions Map should contain a
     * {@link TaskObject} with the object instance and the method subscribed as
     * a map's key</li>
     * <li>And the {@link TaskObject} subscriptions Map should contain the
     * {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingAnExistingTaskToAnExistingTopicShouldShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
            Map<AbstractTask, Topic> subscriptionsMap = baboonConfig.getSubscriptionsUnmodifiableMap();
            TaskObject testTO = new TaskObject(mockController, MethodDictionary.getMethod(mockController, taskMethod));
            assertEquals(1, subscriptionsMap.size());
            assertTrue(subscriptionsMap.keySet().contains(testTO));
            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testTO).getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingAMethodThatIsNotAnnotatedToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String method = "mockNotSubscribableMethod";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("The method should be annotated with HappeningHandler or Task annotations"));
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a null method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingANullMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, null);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("Cannot subscribe a null method name"));
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a method name that
     * does not exists on the object to a {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingANotExistingMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, "methodNotExistingOnClass");
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("This method does not exist on object provided"));
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a not
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingToANotExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic("notExistingTopicName", mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
        try {
            baboonConfig.subscribeToTopic(null, mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("Cannot subscribe to a null topic"));
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a null object and a string as name of method to an
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingNullObjectToExistingTopicShouldNotGetRegisteredInConfigTest() {
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], null, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("Cannot subscribe a null object"));
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link HappeningHandler}</li>
     * <li>And the instance of controller object also has a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe more than one {@link TaskObject} or
     * {@link HappeningHandlerObject} to the same {@link Topic} in any
     * combinations</li>
     * <li>Then all the {@link HappeningHandlerObject} and {@link TaskObject}
     * objects should be subscribed
     */
    @Test
    public void subscribingMoreThanOneHappeningHandlerOrTaskToTheSameTopicShouldBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final String taskMethod = "mockTask";
        final String happeningHandlerMethod2 = "mockHappeningHandler2";
        final String taskMethod2 = "mockTask2";

        // Subscribing HappeningHandler and then Task should be possible
        BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            assertEquals(1, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(2, baboonConfig.getSubscriptionsUnmodifiableMap().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing Task and then HappeningHandler should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(1, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
            assertEquals(2, baboonConfig.getSubscriptionsUnmodifiableMap().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing Task and then Task should should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(2, baboonConfig.getSubscriptionsUnmodifiableMap().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing HappeningHandler and then HappeningHandler should be
        // possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
            assertEquals(1, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            assertEquals(2, baboonConfig.getSubscriptionsUnmodifiableMap().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskObject} or
     * {@link HappeningHandlerObject} to more than one topic</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingSameTaskOrHappeningHandlerToMoreThanOneTopicShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("The task is already subscribed to a topic"));
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
            assertEquals(2, baboonConfig.getSubscriptionsUnmodifiableMap().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("The happening handler is already subscribed to a topic"));
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And topic1 has an empty permission string</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskObject} to topic1</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingTaskToTopicWithEmptyPermissionShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission());
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("The topic's permission cannot be empty"));
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic2"</li>
     * <li>And topic2 has not the permission field</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskObject} to topic2</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingTaskToTopicWithNullPermissionShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals(null, baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission());
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("The topic's permission cannot be null"));
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And topic1 has an empty permission string</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningHandlerObject} to topic1</li>
     * <li>Then the {@link HappeningHandlerObject} subscriptions Map should
     * contain a {@link HappeningHandlerObject} with the object instance and the
     * method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerObject} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningHandlerToTopicWithEmptyPermissionShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission());
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
            Map<AbstractTask, Topic> subscriptionsMap = baboonConfig.getSubscriptionsUnmodifiableMap();
            HappeningHandlerObject testHHO = new HappeningHandlerObject(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            assertEquals(1, subscriptionsMap.size());
            assertTrue(subscriptionsMap.keySet().contains(testHHO));
            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testHHO).getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic2"</li>
     * <li>And topic2 has not the permission field</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningHandlerObject} to topic2</li>
     * <li>Then the {@link HappeningHandlerObject} subscriptions Map should
     * contain a {@link HappeningHandlerObject} with the object instance and the
     * method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerObject} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningHandlerToTopicWithNullPermissionShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals(null, baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission());
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
            Map<AbstractTask, Topic> subscriptionsMap = baboonConfig.getSubscriptionsUnmodifiableMap();
            HappeningHandlerObject testHHO = new HappeningHandlerObject(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            assertEquals(1, subscriptionsMap.size());
            assertTrue(subscriptionsMap.keySet().contains(testHHO));
            assertEquals(topicNamesDefined[1], subscriptionsMap.get(testHHO).getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic3"</li>
     * <li>And topic3 has a set_guard_callback {{["g1","g2"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the same controller has a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g2"</li>
     * <li>When I subscribe a {@link HappeningHandlerObject} to topic3</li>
     * <li>Then the {@link HappeningHandlerObject} subscriptions Map should
     * contain a {@link HappeningHandlerObject} with the object instance and the
     * method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerObject} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     * <li>And {@link HappeningHandlerObject#getGuardCallback(String)} should
     * return a guard callback method with for "g1"</li> *
     * <li>And {@link HappeningHandlerObject#getGuardCallback(String)} should
     * return a guard callback method with for "g2"</li>
     */
    @Test
    public void subscribingAbstractTaskWithGuardProvidersToTopicWithGuardCallbackShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final String providerMethod1 = "mockGuard1Provider";
        final String providerMethod2 = "mockGuard2Provider";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().contains("g1"));
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().contains("g2"));
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            Map<AbstractTask, Topic> subscriptionsMap = baboonConfig.getSubscriptionsUnmodifiableMap();
            HappeningHandlerObject testHHO = new HappeningHandlerObject(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            assertEquals(1, subscriptionsMap.size());
            assertTrue(subscriptionsMap.keySet().contains(testHHO));
            for (AbstractTask key : subscriptionsMap.keySet()) {
                if (key.equals(testHHO)) {
                    assertEquals(providerMethod1, key.getGuardCallback("g1").getName());
                    assertEquals(providerMethod2, key.getGuardCallback("g2").getName());
                }
            }
            assertEquals(topicNamesDefined[2], subscriptionsMap.get(testHHO).getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic4"</li>
     * <li>And topic4 has a set_guard_callback {{["g1","g3"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the controller has not a method annotated with
     * {@link GuardProvider#value()} "g3"</li>
     * <li>When I subscribe a {@link HappeningHandlerObject} to topic4</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingAnAbstractTaskWithMissingGuardProviderToTopicWithGuardCallbackShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod = "mockTask";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().contains("g1"));
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().contains("g3"));
            baboonConfig.subscribeToTopic(topicNamesDefined[3], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
            assertTrue(e.getMessage().contains("There is not a GuardProvider annotated method with value"));
        }
    }

}
