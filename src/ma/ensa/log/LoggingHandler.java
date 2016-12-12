package ma.ensa.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import ma.ensa.model.Compte;

public class LoggingHandler implements InvocationHandler {
	private Logger rootLogger = Logger.getLogger(this.getClass().getName());
	private Object target;

	public LoggingHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		Compte compte = (Compte) target;
		double mt = (double) args[0];

		/*
		 * Verser
		 */
		if (method.getName() == "verser") {
			/*
			 * Obtenir le solde avant virement
			 */
			rootLogger.fatal("Solde actuel : " + compte.getSolde());
			rootLogger.fatal("Virement de " + mt);

			/*
			 * Appel de verser
			 */
			result = method.invoke(target, mt);

			/*
			 * Obtenir le solde après virement
			 */
			rootLogger.fatal("Solde actualisé après le virement de " + mt + " : " + compte.getSolde());

			/*
			 * Retirer
			 */
		} else if (method.getName() == "retirer") {
			/*
			 * Obtenir le solde avant retrait
			 */
			rootLogger.fatal("Solde actuel : " + compte.getSolde());
			rootLogger.fatal("Retrait de " + mt);

			if (mt <= compte.getSolde()) {
				result = method.invoke(target, mt);
				rootLogger.fatal("Solde actualisé après le retrait de " + mt + " : " + compte.getSolde());
			} else {
				rootLogger.fatal("Impossible de retirer " + mt + " du solde actuel de : " + compte.getSolde());
			}
			/*
			 * Obtenir le solde après retrait
			 */
			rootLogger.fatal("Solde actuel : " + compte.getSolde());
		} else {
			rootLogger.fatal("Erreur detecté!");
		}

		return result;
	}

}
