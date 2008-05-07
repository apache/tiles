package org.apache.tiles.evaluator.el;

import javax.el.ExpressionFactory;

import org.apache.el.ExpressionFactoryImpl;

public class TomcatExpressionFactoryFactory implements ExpressionFactoryFactory {

    public ExpressionFactory getExpressionFactory() {
        return new ExpressionFactoryImpl();
    }
}
