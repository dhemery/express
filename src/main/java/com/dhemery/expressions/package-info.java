/**
 * Expression Kit provides methods to compose conditions, evaluate them, and act
 * on the results. These expression methods designed to satisfy three goals:
 * <ul>
 * <li>Evaluate the conditions they express</li>
 * <li>Read nicely in the code where they are called</li>
 * <li>Support diagnosis by fully describing the conditions in error, exception,
 * and log messages</li>
 * </ul>
 *
 * <h1>Expression Methods</h1>
 * <p>
 * Expression Kit defines four types of expression methods,
 * each with a distinct purpose:
 *
 * <table summary="Types of Expressions" class="daleTable">
 * <thead>
 * <tr>
 * <th>Method name</th>
 * <th>Purpose</th>
 * <th>Result If Satisfied</th>
 * <th>Result If Not Satisfied</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>{@code assertThat}</td>
 * <td>Assert that a condition is true</td>
 * <td>Returns</td>
 * <td>Throws an {@link java.lang.AssertionError}</td>
 * </tr>
 * <tr>
 * <td>{@code satisfiedThat}</td>
 * <td>Indicate whether a condition is true</td>
 * <td>Returns {@code true}
 * <td>Returns {@code false}
 * </tr>
 * <tr>
 * <td>{@code waitUntil}</td>
 * <td>Wait until a condition is true</td>
 * <td>Returns</td>
 * <td>Throws a {@link com.dhemery.expressions.polling.PollTimeoutException PollTimeoutException}</td>
 * </tr>
 * <tr>
 * <td>{@code when}</td>
 * <td>Wait until a subject satisfies a precondition before acting on it</td>
 * <td>Returns its subject</td>
 * <td>Throws a {@link com.dhemery.expressions.polling.PollTimeoutException PollTimeoutException}</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * Each expression method is named to read nicely in the code where it is called.
 * For example, each of these calls evaluates whether the submit button is displayed:
 *
 * <pre>
 * assertThat(submitButton, isDisplayed);
 *
 * if(satisfiedThat(submitButton, isDisplayed)) { /* do something /* }
 *
 * waitUntil(submitButton, isDisplayed);
 *
 * when(submitButton, isDisplayed).click();
 * </pre>
 *
 * <h1>Composing Conditions</h1>
 * <p>
 * Each expression method has a number of forms. Each form offers a distinct
 * way to compose the condition for the method to evaluate.
 *
 * <table summary="Composed Conditions" class="daleTable">
 * <thead>
 * <tr>
 * <th>Parameters</th>
 * <th>Composes a Condition That is Satisfied If ...</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>{@code supplier}</td>
 * <td>{@code supplier.getAsBoolean()} returns {@code true}</td>
 * </tr>
 * <tr>
 * <td>{@code subject}, {@code matcher}</td>
 * <td>{@code matcher.matches(subject)} returns {@code true}</td>
 * </tr>
 * <tr>
 * <td>{@code subject}, {@code predicate}</td>
 * <td>{@code predicate.test(subject)} returns {@code true}</td>
 * </tr>
 * <tr>
 * <td>{@code subject}, {@code function}, {@code matcher}</td>
 * <td>{@code matcher.matches(function.apply(subject))} returns {@code true}</td>
 * </tr>
 * <tr>
 * <td>{@code subject}, {@code function}, {@code predicate}</td>
 * <td>{@code predicate.test(function.apply(subject))} returns {@code true}</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * For example, each of these these examples evaluates some condition relating
 * to a submit button:
 * <pre>
 * assertThat(submitButton, isDisplayed);
 *
 * assertThat(submitButton, backgroundColor, is(standardButtonBackgroundColor));
 * </pre>
 *
 * <h1>Self-Describing Conditions</h1>
 * Most expression methods are designed not only to evaluate conditions, but
 * also to describe the conditions they evaluate. For example, when each
 * {@code assertThat}, {@code waitUntil} and {@code when} method throws an error
 * or exception, it includes a description of the condition. Also, each polled
 * expression method is designed to allow users to implement a
 * {@link com.dhemery.expressions.Poller Poller}
 * that logs each evaluation. Such a logging poller may want to describe the
 * polled conditions in its log messages.
 * <p>
 * In order to ensure that these expression methods can fully describe the
 * conditions they evaluate, they accept only supplier, function, and predicate
 * objects that are able to describe themselves. To support this requirement,
 * Expression Kit defines {@link org.hamcrest.SelfDescribing} specializations of
 * three standard Java functional interfaces:
 * <ul>
 * <li>{@link com.dhemery.expressions.SelfDescribingBooleanSupplier}</li>
 * <li>{@link com.dhemery.expressions.SelfDescribingFunction}</li>
 * <li>{@link com.dhemery.expressions.SelfDescribingPredicate}</li>
 * </ul>
 * <p>
 * The only expression methods that can accept bare, non-{@code SelfDescribing}
 * suppliers, functions, and predicates are the
 * {@link com.dhemery.expressions.Expressions#satisfiedThat Expressions.satisfiedThat}
 * methods. These methods do not throw exceptions, and do not poll, and so do
 * not require the objects to describe themselves.
 * <p>
 * <strong>Creating Self-Describing Suppliers, Functions, and Predicates</strong>
 * <p>
 * The {@link com.dhemery.expressions.Named} interface offers factory methods to decorate
 * {@code BooleanSupplier},
 * {@code Function},
 * and {@code Predicate} objects
 * with the ability to describe themselves.
 *
 * <pre>{@literal
 * SelfDescribingFunction<String,Integer> length = Named.function("length", String::length);
 *
 * SelfDescribingPredicate<String> empty = Named.predicate("empty", String::isEmpty);
 * }</pre>
 * <h1>Evaluating Immediately or By Polling</h1>
 * <p>
 * Each expression method has forms that accept a
 * {@link com.dhemery.expressions.PollingSchedule PollingSchedule}
 * and forms that do not.
 * The effect of passing a polling schedule or not passing one
 * depends on the method.
 *
 * <table summary="Expression Methods and Polling" class="daleTable">
 * <thead>
 * <tr>
 * <th>Method Name</th>
 * <th>Without a Polling Schedule</th>
 * <th>With a Polling Schedule</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>{@code assertThat}</td>
 * <td>Evaluates the condition immediately, without polling</td>
 * <td>Evaluates the condition using the given polling schedule</td>
 * </tr>
 * <tr>
 * <td>{@code satisfiedThat}</td>
 * <td>Evaluates the condition immediately, without polling</td>
 * <td>Evaluates the condition using the given polling schedule</td>
 * </tr>
 * <tr>
 * <td>{@code waitUntil}</td>
 * <td>Polls the condition using the default polling schedule</td>
 * <td>Evaluates the condition using the given polling schedule</td>
 * </tr>
 * <tr>
 * <td>{@code when}</td>
 * <td>Polls the condition using the default polling schedule</td>
 * <td>Evaluates the condition using the given polling schedule</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <p>
 * <strong>Explicit Polling</strong>
 * <p>
 * When a polling schedule is passed as the first parameter to any expression
 * method, the method evaluates the condition repeatedly, until either the
 * condition is satisfied or the polling schedule's duration expires.
 * <p>
 * For example, each of these calls polls for up to 10 seconds for the submit
 * button to be displayed:
 * <pre>
 * assertThat(within(10, SECONDS), submitButton, isDisplayed);
 *
 * if(satisfiedThat(within(10, SECONDS), submitButton, isDisplayed) { ... }
 *
 * waitUntil(within(10, SECONDS), submitButton, isDisplayed);
 *
 * when(within(10, SECONDS), submitButton, isDisplayed).click();
 * </pre>
 * <p>
 * <strong>Implicit Polling</strong>
 * <p>
 * Two types of expression methods, {@code waitUntil} and {@code when},
 * are always polled. When called with out a polling schedule, these methods
 * implicitly use the <em>default polling schedule</em> (see below), which they
 * obtain by calling the
 * {@link com.dhemery.expressions.PolledExpressions#eventually eventually}
 * method.
 * <p>
 * Each of these evaluates the condition repeatedly until it is satisfied,
 * using the default polling schedule:
 * <pre>
 * waitUntil(submitButton, isDisplayed);
 *
 * when(submitButton, isDisplayed).click();
 * </pre>
 * <p>
 * <strong>Evaluating Immediately</strong>
 * <p>
 * Two types of expression methods, {@code assertThat} and {@code satisfiedThat},
 * poll only when passed a polling schedule. When called without a polling
 * schedule, these methods evaluate the condition immediately, without polling.
 * <p>
 * Each of these calls evaluates the condition immediately, without polling:
 * <pre>
 * assertThat(submitButton, isDisplayed);
 *
 * if(satisfiedThat(submitButton, isDisplayed) { ... }
 * </pre>
 *
 * <h1>Polling Schedules</h1>
 * <p>
 * A polling schedule includes a polling interval and a polling duration.
 * The interval indicates how often to evaluate the condition,
 * and the duration indicates how long to poll.
 * </p>
 * <p><strong>The Default Polling Schedule</strong>
 * </p>
 * <p>Each implementation of {@link com.dhemery.expressions.PolledExpressions}
 * must implement the {@code eventually} method.
 * This method returns a default polling schedule.
 * </p>
 * <p>This default polling schedule is used implicitly whenever a {@code waitUntil} or
 * {@code when} method is called without a polling schedule:
 * <pre>
 * waitUntil(submitButton, isDisplayed);
 *
 * when(submitButton, isDisplayed).click();
 * </pre>
 * <p>
 * The default polling schedule can also be passed explicitly by calling
 * {@code eventually}:
 * </p>
 * <pre>
 * assertThat(eventually(), submitButton, isDisplayed);
 *
 * if(satisfiedThat(eventually(), submitButton, isDisplayed) { ... }
 *
 * // Explicit but otherwise unnecessary
 * waitUntil(eventually(), submitButton, isDisplayed);
 *
 * // Explicit but otherwise unnecessary
 * when(eventually(), submitButton, isDisplayed).click();
 * </pre>
 *
 * <h1>Time Frames</h1>
 * <p>
 * Polled expressions read nicely when the polling schedule is phrased as a time
 * frame. The {@link com.dhemery.expressions.TimeFrames} interface defines
 * several "time frame" factory methods for polling schedules:
 * <table summary="Time Frame Methods" class="daleTable">
 * <thead>
 * <tr>
 * <th>Method Name</th>
 * <th colspan="2" style="text-align: center">Creates a Polling Schedule With</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>{@code eventually}</td>
 * <td>The default polling interval</td>
 * <td>The default polling duration</td>
 * </tr>
 * <tr>
 * <td>{@code within}</td>
 * <td>The default polling interval</td>
 * <td>The given polling duration</td>
 * </tr>
 * <tr>
 * <td>{@code checkingEvery}</td>
 * <td>The given polling interval</td>
 * <td>The default polling duration</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * Each method is designed to read reasonably nicely in polled expressions:
 * <pre>
 * assertThat(eventually(), submitButton, isDisplayed);
 *
 * assertThat(within(10, SECONDS), submitButton, isDisplayed);
 *
 * assertThat(checkingEvery(100, MILLIS), submitButton, isDisplayed);
 * </pre>
 * <p>
 * The polling schedule returned by {@code within} has a {@code checkingEvery}
 * method. This gives a way to specify both the polling interval and duration in
 * a single expression:
 * <pre>
 * assertThat(within(10, SECONDS).checkingEvery(100, MILLIS), submitButton, isDisplayed);
 * </pre>
 */
package com.dhemery.expressions;
