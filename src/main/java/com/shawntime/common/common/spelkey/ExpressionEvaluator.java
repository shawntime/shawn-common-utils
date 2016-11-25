package com.shawntime.common.common.spelkey;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ObjectUtils;

/**
 * Created by IDEA
 * User: mashaohua
 * Date: 2016-10-12 10:07
 * Desc:
 */
class ExpressionEvaluator {

    /**
     * Indicate that there is no result variable.
     */
    public static final Object NO_RESULT = new Object();

    /**
     * Indicate that the result variable cannot be used at all.
     */
    public static final Object RESULT_UNAVAILABLE = new Object();

    /**
     * The name of the variable holding the result object.
     */
    public static final String RESULT_VARIABLE = "result";


    private final SpelExpressionParser parser = new SpelExpressionParser();

    // shared param discoverer since it caches data internally
    private final ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<ExpressionKey, Expression>(64);

    private final Map<MethodCacheKey, Method> targetMethodCache = new ConcurrentHashMap<MethodCacheKey, Method>(64);


    /**
     * Create an {@link EvaluationContext} without a return value.
     * @see #createEvaluationContext(Method, Object[], Object, Class, Object)
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass) {

        return createEvaluationContext(method, args, target, targetClass, NO_RESULT);
    }

    /**
     * Create an {@link EvaluationContext}.
     * @param method the method
     * @param args the method arguments
     * @param target the target object
     * @param targetClass the target class
     * @param result the return value (can be {@code null}) or
     * {@link #NO_RESULT} if there is no return at this time
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass, Object result) {

        KeySpELExpressionRootObject rootObject = new KeySpELExpressionRootObject(method, args, target, targetClass);
        KeySpELEvaluationContext evaluationContext = new KeySpELEvaluationContext(rootObject,
                this.paramNameDiscoverer, method, args, targetClass, this.targetMethodCache);
        if (result == RESULT_UNAVAILABLE) {
            evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
        }
        else if (result != NO_RESULT) {
            evaluationContext.setVariable(RESULT_VARIABLE, result);
        }
        return evaluationContext;
    }

    public Object key(String keyExpression, MethodCacheKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.keyCache, keyExpression, methodKey).getValue(evalContext);
    }

    private Expression getExpression(Map<ExpressionKey, Expression> cache, String expression, MethodCacheKey methodKey) {
        ExpressionKey key = createKey(methodKey, expression);
        Expression expr = cache.get(key);
        if (expr == null) {
            expr = this.parser.parseExpression(expression);
            cache.put(key, expr);
        }
        return expr;
    }

    private ExpressionKey createKey(MethodCacheKey methodCacheKey, String expression) {
        return new ExpressionKey(methodCacheKey, expression);
    }


    private static class ExpressionKey {

        private final MethodCacheKey methodCacheKey;

        private final String expression;

        public ExpressionKey(MethodCacheKey methodCacheKey, String expression) {
            this.methodCacheKey = methodCacheKey;
            this.expression = expression;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ExpressionKey)) {
                return false;
            }
            ExpressionKey otherKey = (ExpressionKey) other;
            return (this.methodCacheKey.equals(otherKey.methodCacheKey) &&
                    ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
        }

        @Override
        public int hashCode() {
            return this.methodCacheKey.hashCode() * 29 + (this.expression != null ? this.expression.hashCode() : 0);
        }
    }

}
