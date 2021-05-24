package com.a1qa.common;

import com.a1qa.model.domain.Test;

/**
 * Created by p.ordenko on 12.06.2015, 18:31.
 */
public enum Page {

    PROJECTS {},
    SESSIONS {
        @Override
        public BreadCrumb getBreadCrumb(Test test) {
            return new BreadCrumb(test.getProject().getName(),
                    "/sessions?projectId=" + test.getProject().getId());
        }

        @Override
        public Page getParent() {
            return PROJECTS;
        }
    },
    TESTS {
        @Override
        public BreadCrumb getBreadCrumb(Test test) {
            return new BreadCrumb(test.getSession().getCreatedTime().toString(),
                    "/tests?sid=" + test.getSession().getId());
        }

        @Override
        public Page getParent() {
            return SESSIONS;
        }
    },
    TEST_INFO {
        @Override
        public BreadCrumb getBreadCrumb(Test test) {
            return new BreadCrumb(test.getName(),
                    "/testInfo?testId=" + test.getId());
        }

        @Override
        public Page getParent() {
            return TESTS;
        }
    },
    TEST_HISTORY {
        @Override
        public BreadCrumb getBreadCrumb(Test test) {
            return new BreadCrumb(test.getName(),
                    "/history?testId=" + test.getId());
        }

        @Override
        public Page getParent() {
            return SESSIONS;
        }
    },
    ALL_TESTS {
        @Override
        public BreadCrumb getBreadCrumb(Test test) {
            return new BreadCrumb(test.getProject().getName(),
                    "/allTests?projectId=" + test.getProject().getId());
        }

        @Override
        public Page getParent() {
            return PROJECTS;
        }
    };


    public BreadCrumb getBreadCrumb(Test test) {
        return null;
    }

    public Page getParent() {
        return PROJECTS;
    }
}
