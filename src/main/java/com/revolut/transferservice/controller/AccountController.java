package com.revolut.transferservice.controller;

import com.google.gson.Gson;
import com.revolut.transferservice.controller.request.TransferRequest;
import com.revolut.transferservice.controller.response.ResponseObject;
import com.revolut.transferservice.controller.response.ResponseStatus;
import com.revolut.transferservice.exception.BadRequest;
import com.revolut.transferservice.exception.InsufficientFunds;
import com.revolut.transferservice.exception.NotFoundException;
import com.revolut.transferservice.dao.AccountDao;
import com.revolut.transferservice.dao.TransferAccountDao;
import com.revolut.transferservice.service.TransferAccountService;
import com.revolut.transferservice.utils.resolver.AccountTemplateResolver;
import spark.Request;
import spark.Response;


public class AccountController {

    public Object transfer(Request request, Response response) {

        response.type("application/json");
        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus(ResponseStatus.ERROR.toString());

        try {
            TransferRequest transferRequest = new Gson().fromJson(request.body(), TransferRequest.class);
            if (!isValidRequest(transferRequest)) {
                throw new BadRequest("Not valid data");
            }
            TransferAccountService transferAccountService = getTransferAccountService();
            transferAccountService.transfer(transferRequest.getIdFrom(), transferRequest.getIdTo(), transferRequest.getAmount());
            responseObject.setMessage(ResponseStatus.SUCCESS.toString());
            response.status(200);
        } catch (BadRequest ex) {
            response.status(400);
            responseObject.setMessage(ex.getMessage());
        } catch (NotFoundException ex) {
            response.status(404);
            responseObject.setMessage(ex.getMessage());
        } catch (InsufficientFunds ex) {
            response.status(403);
            responseObject.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.status(500);
        }
        return responseObject;
    }

    private TransferAccountService getTransferAccountService() {
        AccountDao accountDao = new TransferAccountDao(new AccountTemplateResolver());
        return new TransferAccountService(accountDao);
    }

    private boolean isValidRequest(TransferRequest transferRequest) {
        return  (transferRequest.getIdTo() > 0 && transferRequest.getIdFrom() > 0 && transferRequest.getAmount() > 0);
    }
}
