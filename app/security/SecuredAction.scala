package security

import play.api.mvc.Action
import play.api.mvc.BodyParser
import play.api.mvc.Result
import play.api.mvc.Request
import play.api.mvc.BodyParsers
import play.api.mvc.AnyContent

object SecuredAction {

  def apply[A](bodyParser: BodyParser[A])(block: Request[A] => Result): Action[A] = new Action[A] {
    def parser = bodyParser
    def apply(ctx: Request[A]) = Security.ensureClientVerified(ctx, block(ctx))
  }

  def apply(block: Request[AnyContent] => Result): Action[AnyContent] = {
    Action(BodyParsers.parse.anyContent)(block)
  }

  def apply(block: => Result): Action[AnyContent] = {
    this.apply(_ => block)
  }

}
