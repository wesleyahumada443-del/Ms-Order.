package ms.order.service.infrastructure.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Constants {

    public static class Info {

        public static class Traces {
            public static  final String INIT = "[INIT] - %s";
            public static  final String END = "[END] -  %s";
            public static final String PROCESSED_SUCCESSFULLY = "%s - Message processed successfully. Payload: %s";
            public static final String MESSAGE_RECEIVED = "%s - Message received: %s";
            public static final String MESSAGE_RECEIVED_FROM = "%s - Received from %s";
            public static final String MESSAGE_ID_RECEIVED = "%s - MessageId received: %s";
        }
    }

    public static class Error {
        public static final String MISSING_FIELD = "Missing '%s' field.";

        public static class Exceptions {
            public static final String TASK_REJECTED_ERROR = "%s - Task rejected due to insufficient threads. Message will be reprocessed later: %s";
            public static final String SAVING_TRANSACTIONS_ERROR = "%s - Error saving transactions for message: %s. Error: %s";
            public static final String UNEXPECTED_PROCESSING_MESSAGE_ERROR = "%s - Unexpected while processing message: %s, Error: %s";
            public static final String PROCESSING_FORMAT_ERROR = "%s - Error: Message sent has incorrect format! Exception: %s - payload: %s";
        }

        public static class Validations {
            public static final String PROCESSING_ERROR = "%s - Error: Message sent has incorrect format! Exception: %s - payload: %s";
        }
    }
}
