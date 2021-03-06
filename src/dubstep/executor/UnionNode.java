package dubstep.executor;

import dubstep.utils.Tuple;

public class UnionNode extends BaseNode {

    private boolean innerDone = false;

    public UnionNode(BaseNode innerNode, BaseNode outerNode) {
        super();
        this.innerNode = innerNode;
        this.innerNode.parentNode = this;
        this.outerNode = outerNode;
        this.outerNode.parentNode = this;
        this.initProjectionInfo();
    }

    @Override
    Tuple getNextRow() {
        if (!innerDone) {
            Tuple tuple = innerNode.getNextTuple();
            if (tuple == null) {
                innerDone = true;
                return outerNode.getNextTuple();
            }
            return tuple;
        } else {
            return outerNode.getNextTuple();
        }
    }

    @Override
    void resetIterator() {
        innerNode.resetIterator();
        outerNode.resetIterator();
    }

    @Override
    void initProjectionInfo() {
        projectionInfo = innerNode.projectionInfo;
        typeList = innerNode.typeList;
        //FIXME: this is most probably broken but we somehow passed CP1. There's no union in CP2 yet.
    }

    @Override
    public void initProjPushDownInfo() {
        this.requiredList = this.parentNode.requiredList;
        this.innerNode.initProjPushDownInfo();
    }
}
