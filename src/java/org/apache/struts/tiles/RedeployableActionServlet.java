package org.apache.struts.tiles;

import javax.servlet.ServletException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.DefinitionsFactory;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.TilesRequestProcessor;


/**
 * <p>
 * WebLogic (at least v6 and v7) attempts to serialize the TilesRequestProcessor
 * when re-deploying the Webapp in development mode. The TilesRequestProcessor
 * is not serializable, and loses the Tiles definitions. This results in
 * NullPointerException and/or NotSerializableException when using the app after
 * automatic redeploy.
 * </p>
 * <p>
 * This bug report proposes a workaround for this problem, in the hope it will
 * help others and maybe motivate an actual fix.
 * </p>
 * <p>
 * The attached class extends the Struts Action servlet and fixes the problem by
 * reloading the Tiles definitions when they have disappeared.
 * </p>
 * <p>
 * For background discussion see
 * http://issues.apache.org/bugzilla/show_bug.cgi?id=26322
 * </p>
 * @version $Rev$ $Date$
 * @since 1.2.1
 */
public class RedeployableActionServlet extends ActionServlet {
    private TilesRequestProcessor tileProcessor;

    protected synchronized RequestProcessor
            getRequestProcessor(ModuleConfig config) throws ServletException {

        if (tileProcessor != null) {
            TilesRequestProcessor processor = (TilesRequestProcessor) super.getRequestProcessor(config);
            return processor;
        }

        // reset the request processor
        String requestProcessorKey = Globals.REQUEST_PROCESSOR_KEY +
                config.getPrefix();
        getServletContext().removeAttribute(requestProcessorKey);

        // create a new request processor instance
        TilesRequestProcessor processor = (TilesRequestProcessor) super.getRequestProcessor(config);

        tileProcessor = processor;

        try {
            // reload Tiles defs
            DefinitionsFactory factory = processor.getDefinitionsFactory();
            factory.init(factory.getConfig(), getServletContext());
            // System.out.println("reloaded tiles-definitions");
        } catch (DefinitionsFactoryException e) {
            e.printStackTrace();
        }

        return processor;
    }
}
