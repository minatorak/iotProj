package com.example.sirirak.iotproj.model;


public class dataclass {
    public Diff diff;

    public static class Diff {
        public int near;

        public int getNear() {
            return near;
        }

        public void setNear(int near) {
            this.near = near;
        }
    }

    public PClient pClient;

    public static class PClient {
        public int lati;
        public int longti;

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
    public static class PNode{
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

    public ProcessStage process;
    public static class ProcessStage{
        public String codeValidate;
        public int state;
        public int validate;

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

    public ProcessStage getProcess() {
        return process;
    }

    public void setProcess(ProcessStage processStage) {
        this.process = processStage;
    }
}
