package com.example.sirirak.iotproj.model;


public class dataclass {
    public Diff diff;

    private static class Diff {
        private int near;

        public int getNear() {
            return near;
        }

        public void setNear(int near) {
            this.near = near;
        }
    }

    public PClient pClient;

    private static class PClient {
        private int lati;
        private int longti;

        public int getLati() {
            return lati;
        }

        public void setLati(int lati) {
            this.lati = lati;
        }

        public int getLongti() {
            return longti;
        }

        public void setLongti(int longti) {
            this.longti = longti;
        }
    }

    public PNode pNode;
    private static class PNode{
        private int lati;
        private int longti;

        public int getLati() {
            return lati;
        }

        public void setLati(int lati) {
            this.lati = lati;
        }

        public int getLongti() {
            return longti;
        }

        public void setLongti(int longti) {
            this.longti = longti;
        }
    }

    public ProcessStage processStage;
    private static class ProcessStage{
        private String codeValidate;
        private int state;
        private int validate;

        public String getCodeValidate() {
            return codeValidate;
        }

        public void setCodeValidate(String codeValidate) {
            this.codeValidate = codeValidate;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getValidate() {
            return validate;
        }

        public void setValidate(int validate) {
            this.validate = validate;
        }
    }

    public Diff getDiff() {
        return diff;
    }

    public void setDiff(Diff diff) {
        this.diff = diff;
    }

    public PClient getpClient() {
        return pClient;
    }

    public void setpClient(PClient pClient) {
        this.pClient = pClient;
    }

    public PNode getpNode() {
        return pNode;
    }

    public void setpNode(PNode pNode) {
        this.pNode = pNode;
    }

    public ProcessStage getProcessStage() {
        return processStage;
    }

    public void setProcessStage(ProcessStage processStage) {
        this.processStage = processStage;
    }
}
