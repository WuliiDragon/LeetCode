pragma solidity ^0.4.19;


//四则运算
library SafeMath {
    function mul(uint256 a, uint256 b) internal pure returns (uint256) {
        if (a == 0) {
            return 0;
        }
        uint256 c = a * b;
        assert(c / a == b);
        return c;
    }

    function div(uint256 a, uint256 b) internal pure returns (uint256) {
        // assert(b > 0); // Solidity automatically throws when dividing by 0
        uint256 c = a / b;
        // assert(a == b * c + a % b); // There is no case in which this doesn't hold
        return c;
    }

    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        assert(b <= a);
        return a - b;
    }

    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }
}


//锁定
library Locking {

    //给 uint256类型使用四则运算
    using SafeMath for uint256;

    struct LockingBox {

        //锁定时间
        uint256 releaseTimeStamp;

        uint256 amount;
    }

    struct LockingBoxes {

        //未锁定的代币数量
        uint256 unlockedSupply;

        //锁定的代币数量
        uint256 lockedSupply;

        //存放预留代币
        mapping (string => LockingBox) boxes;
    }



    //响应事件 解锁代币和锁定预留代币
    event LockSupply(string name, uint256 amount, uint256 releaseTimeStamp);

    event UnlockSupply(string name, uint256 amount);

    function add(LockingBoxes storage self, uint256 amount_) public {
        require(amount_ > 0);
        self.unlockedSupply = self.unlockedSupply.add(amount_);
    }

    function retrieve(LockingBoxes storage self, uint256 amount_) public {
        require(amount_ > 0);
        require(self.unlockedSupply >= amount_);

        self.unlockedSupply = self.unlockedSupply.sub(amount_);
    }


    //锁定预留代币  提供名字  账户  锁定时间（单位为时间）
    //这里将LockingBoxes 替代为self
    function lock(LockingBoxes storage self, string name_, uint256 amount_, uint256 lockHours_) public {
        require(self.boxes[name_].amount == 0);
        require(amount_ > 0);
        require(lockHours_ > 0);
        require(self.unlockedSupply >= amount_);


        //锁定到未锁定转移
        self.unlockedSupply = self.unlockedSupply.sub(amount_);
        self.lockedSupply = self.lockedSupply.add(amount_);

        self.boxes[name_].amount = amount_;
        self.boxes[name_].releaseTimeStamp = now + lockHours_ * 1 hours;

        LockSupply(name_, amount_, self.boxes[name_].releaseTimeStamp);
    }


    //解锁  只需提供
    function unlock(LockingBoxes storage self, string name_) public {
        require(self.boxes[name_].amount > 0);
        require(now >= self.boxes[name_].releaseTimeStamp);

        uint256 amount = self.boxes[name_].amount;
        self.unlockedSupply = self.unlockedSupply.add(amount);
        self.lockedSupply = self.lockedSupply.sub(amount);

        delete self.boxes[name_];

        UnlockSupply(name_, amount);
    }

    function getSupply(LockingBoxes storage self) view public returns (uint256, uint256) {
        return (self.unlockedSupply, self.lockedSupply);
    }
}


/**
 * @title ERC20Basic
 * @dev Simpler version of ERC20 interface
 * @dev see https://github.com/ethereum/EIPs/issues/179
 */
contract ERC20Basic {

    //代币总数目
    uint256 public totalSupply;

    //取余额
    function balanceOf(address who) public view returns (uint256);

    //由调用者发起交易
    function transfer(address to, uint256 value) public returns (bool);

    //出发事件
    event Transfer(address indexed from, address indexed to, uint256 value);
}


/**
 * @title ERC20 interface
 * @dev see https://github.com/ethereum/EIPs/issues/20
 */
contract ERC20 is ERC20Basic {

    //允许
    function allowance(address owner, address spender) public view returns (uint256);

    //
    function transferFrom(address from, address to, uint256 value) public returns (bool);

    //
    function approve(address spender, uint256 value) public returns (bool);


    //批准响应事件
    event Approval(address indexed owner, address indexed spender, uint256 value);
}


contract DetailedERC20 is ERC20 {

    //代币名称
    string public name;

    //代币图标
    string public symbol;

    //代币小数点位数
    uint8 public decimals;

    //构造方法
    function DetailedERC20(string _name, string _symbol, uint8 _decimals) public {
        name = _name;
        symbol = _symbol;
        decimals = _decimals;
    }
}


/**
 * @title Basic token
 * @dev Basic version of StandardToken, with no allowances.
 */
contract BasicToken is ERC20Basic {
    using SafeMath for uint256;


    //存账户余额的map
    mapping (address => uint256) balances;

    /**
    * @dev transfer token for a specified address
    * @param _to The address to transfer to.
    * @param _value The amount to be transferred.
    */
    function transfer(address _to, uint256 _value) public returns (bool) {
        require(_to != address(0));
        require(_value <= balances[msg.sender]);

        // SafeMath.sub will throw if there is not enough balance.
        balances[msg.sender] = balances[msg.sender].sub(_value);
        balances[_to] = balances[_to].add(_value);
        Transfer(msg.sender, _to, _value);
        return true;
    }

    /**
    * @dev Gets the balance of the specified address.
    * @param _owner The address to query the the balance of.
    * @return An uint256 representing the amount owned by the passed address.
    */
    function balanceOf(address _owner) public view returns (uint256 balance) {
        return balances[_owner];
    }

}


/**
 * @title Standard ERC20 token
 *
 * @dev Implementation of the basic standard token.
 * @dev https://github.com/ethereum/EIPs/issues/20
 * @dev Based on code by FirstBlood: https://github.com/Firstbloodio/token/blob/master/smart_contract/FirstBloodToken.sol
 */
contract StandardToken is ERC20, BasicToken {


    //允许？？
    mapping (address => mapping (address => uint256)) internal allowed;


    /**
     * @dev Transfer tokens from one address to another
     * @param _from address The address which you want to send tokens from
     * @param _to address The address which you want to transfer to
     * @param _value uint256 the amount of tokens to be transferred
     */
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool) {
        require(_to != address(0));
        require(_value <= balances[_from]);
        require(_value <= allowed[_from][msg.sender]);

        balances[_from] = balances[_from].sub(_value);
        balances[_to] = balances[_to].add(_value);
        allowed[_from][msg.sender] = allowed[_from][msg.sender].sub(_value);
        Transfer(_from, _to, _value);
        return true;
    }

    /**
     * @dev Approve the passed address to spend the specified amount of tokens on behalf of msg.sender.
     *
     * Beware that changing an allowance with this method brings the risk that someone may use both the old
     * and the new allowance by unfortunate transaction ordering. One possible solution to mitigate this
     * race condition is to first reduce the spender's allowance to 0 and set the desired value afterwards:
     * https://github.com/ethereum/EIPs/issues/20#issuecomment-263524729
     * @param _spender The address which will spend the funds.
     * @param _value The amount of tokens to be spent.
     */
    function approve(address _spender, uint256 _value) public returns (bool) {
        allowed[msg.sender][_spender] = _value;
        Approval(msg.sender, _spender, _value);
        return true;
    }

    /**
     * @dev Function to check the amount of tokens that an owner allowed to a spender.
     * @param _owner address The address which owns the funds.
     * @param _spender address The address which will spend the funds.
     * @return A uint256 specifying the amount of tokens still available for the spender.
     */
    function allowance(address _owner, address _spender) public view returns (uint256) {
        return allowed[_owner][_spender];
    }

    /**
     * @dev Increase the amount of tokens that an owner allowed to a spender.
     *
     * approve should be called when allowed[_spender] == 0. To increment
     * allowed value is better to use this function to avoid 2 calls (and wait until
     * the first transaction is mined)
     * From MonolithDAO Token.sol
     * @param _spender The address which will spend the funds.
     * @param _addedValue The amount of tokens to increase the allowance by.
     */


    //增加批准
    function increaseApproval(address _spender, uint _addedValue) public returns (bool) {
        allowed[msg.sender][_spender] = allowed[msg.sender][_spender].add(_addedValue);
        Approval(msg.sender, _spender, allowed[msg.sender][_spender]);
        return true;
    }

    /**
     * @dev Decrease the amount of tokens that an owner allowed to a spender.
     *
     * approve should be called when allowed[_spender] == 0. To decrement
     * allowed value is better to use this function to avoid 2 calls (and wait until
     * the first transaction is mined)
     * From MonolithDAO Token.sol
     * @param _spender The address which will spend the funds.
     * @param _subtractedValue The amount of tokens to decrease the allowance by.
     */


    //减少批准
    function decreaseApproval(address _spender, uint _subtractedValue) public returns (bool) {
        uint oldValue = allowed[msg.sender][_spender];
        if (_subtractedValue > oldValue) {
            allowed[msg.sender][_spender] = 0;
        }
        else {
            allowed[msg.sender][_spender] = oldValue.sub(_subtractedValue);
        }
        Approval(msg.sender, _spender, allowed[msg.sender][_spender]);
        return true;
    }

}


/**
 * @title Ownable
 * @dev The Ownable contract has an owner address, and provides basic authorization control
 * functions, this simplifies the implementation of "user permissions".
 */

//所有者
contract Ownable {

    //合约发起人
    address public owner;

    //合约所有权转移响应事件
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);


    /**
     * @dev The Ownable constructor sets the original `owner` of the contract to the sender
     * account.
     */

    //构造方法  所有者为调用者
    function Ownable() public {
        owner = msg.sender;
    }


    /**
     * @dev Throws if called by any account other than the owner.
     */

    //检查调用者是否为所有者
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }


    /**
     * @dev Allows the current owner to transfer control of the contract to a newOwner.
     * @param newOwner The address to transfer ownership to.
     */

    //所有权转换
    function transferOwnership(address newOwner) public onlyOwner {

        //且不能是 address(0)
        require(newOwner != address(0));
        OwnershipTransferred(owner, newOwner);
        owner = newOwner;
    }

}


/**
 * @title Pausable
 * @dev Base contract which allows children to implement an emergency stop mechanism.
 */

//提供暂停功能   只维护了一个变量paused
contract Pausable is Ownable {

    //暂停和重新允许事件
    event Pause();

    event Unpause();

    //标识是否暂停？
    bool public paused = false;


    /**
     * @dev Modifier to make a function callable only when the contract is not paused.
     */

    //当没暂停
    modifier whenNotPaused() {
        require(!paused);
        _;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is paused.
     */

    //暂停检查
    modifier whenPaused() {
        require(paused);
        _;
    }

    /**
     * @dev called by the owner to pause, triggers stopped state
     */
    function pause() onlyOwner whenNotPaused public {
        paused = true;
        Pause();
    }

    /**
     * @dev called by the owner to unpause, returns to normal state
     */
    function unpause() onlyOwner whenPaused public {
        paused = false;
        Unpause();
    }
}


/**
 * @title Pausable token
 *
 * @dev StandardToken modified with pausable transfers.
 **/

//
contract PausableToken is StandardToken, Pausable {

    function transfer(address _to, uint256 _value) public whenNotPaused returns (bool) {
        return super.transfer(_to, _value);
    }

    function transferFrom(address _from, address _to, uint256 _value) public whenNotPaused returns (bool) {
        return super.transferFrom(_from, _to, _value);
    }

    function approve(address _spender, uint256 _value) public whenNotPaused returns (bool) {
        return super.approve(_spender, _value);
    }

    function increaseApproval(address _spender, uint _addedValue) public whenNotPaused returns (bool success) {
        return super.increaseApproval(_spender, _addedValue);
    }

    function decreaseApproval(address _spender, uint _subtractedValue) public whenNotPaused returns (bool success) {
        return super.decreaseApproval(_spender, _subtractedValue);
    }
}


contract RawToken is PausableToken {
    using SafeMath for uint256;

    //
    uint256 public freeSupply;
}


//售卖相关
contract OnSaleToken is RawToken {
    using SafeMath for uint256;

    //售卖类型 私募，公募，停止
    enum State {Private, Public, Stop}
    //TODO: Add some event


    //每轮售卖的开启时间，结束时间，售卖额度。？？？
    struct Qualify {
    uint256 epoch;
    uint256 limit;
    uint256 price;
    }


    //存放每轮售卖开始和结束的信息
    mapping (address => Qualify) internal allowed;


    //标识售卖的状态，默认为停止
    State public state = State.Stop;


    //开始时间
    uint256 private startTime;

    //结束时间
    uint256 private endTime;


    //？？？保留
    uint256 public remainSupply;

    //价格？？公募售卖有价格
    uint256 public price;

    //售卖代数
    uint256 internal saleEpoch = 0;
    // start and end timestamps where investments are allowed (both inclusive)



    //设置售卖类型，指出售卖的开始时间，结束时间，_remainSupply？？ 以及售卖类型
    function setSale(uint256 _startTime, uint256 _endTime, uint256 _remainSupply, State _state) internal onlyOwner {

        //停止就不管
        require(state == State.Stop);

        //若开始时间大于？？
        require(_startTime >= block.number);

        //结束时间大于开始时间，不合法
        require(_endTime >= _startTime);


        //？？？？
        require(_state == State.Private || _state == State.Public);


        require(_remainSupply <= freeSupply);
        require(remainSupply == 0);


        //开启一次售卖
        state = _state;
        startTime = _startTime;
        endTime = _endTime;
        remainSupply = _remainSupply;
        freeSupply = freeSupply.sub(_remainSupply);
        saleEpoch = saleEpoch.add(1);

        //freeSupply = freeSupply - _remainSupply？？？
    }

    //开始私募
    function setPrivateSale(uint256 _startTime, uint256 _endTime, uint256 _remainSupply) public onlyOwner {
        setSale(_startTime, _endTime, _remainSupply, State.Private);
    }

    //开始公募
    function setPublicSale(uint256 _startTime, uint256 _endTime, uint256 _remainSupply, uint256 _price) public onlyOwner {
        setSale(_startTime, _endTime, _remainSupply, State.Public);
        require(_price > 0);
        price = _price;
    }


    //？？
    function addQualified(address _address, uint256 _limit, uint256 _price) public onlyOwner whenPrivate {
        require(_address != address(0x0));
        require(_limit > 0);
        require(_price > 0);
        allowed[_address].limit = _limit;
        allowed[_address].price = _price;
        allowed[_address].epoch = saleEpoch;
    }


    //停止私募公募后会清除 将售卖状态全部变为了默认的值
    function clear() internal onlyOwner {
        state = State.Stop;
        startTime = 0;
        endTime = 0;
        price = 0;
        freeSupply = freeSupply.add(remainSupply);
        remainSupply = 0;
    }

    //停止私募
    function stopSale() public onlyOwner {
        require(remainSupply == 0 || block.number > endTime || block.number < startTime);
        clear();
    }

    //暴力清除
    function forceStopSale() public onlyOwner {
        clear();
    }

    //函数检查   能买？
    modifier canBuy() {
        require(state != State.Stop);
        require(block.number >= startTime && block.number <= endTime);
        //Within Period
        require(msg.value > 0);
        uint256 weiAmount = msg.value;
        uint256 tokens = 0;
        if (state == State.Private) {
            require(allowed[msg.sender].epoch == saleEpoch);
            require(allowed[msg.sender].limit > 0);
            tokens = weiAmount.mul(allowed[msg.sender].price);
            require(tokens <= allowed[msg.sender].limit);
        }
        else if (state == State.Public) {
            tokens = weiAmount.mul(price);
        }
        require(tokens <= remainSupply);
        _;
    }

    modifier whenPrivate() {
        require(state == State.Private);
        _;
    }

    modifier whenPublic() {
        require(state == State.Public);
        _;
    }

    modifier whenStopped(){
        require(state == State.Stop);
        _;
    }
}


//预留代币
contract ReservedToken is RawToken {
    using Locking for Locking.LockingBoxes;

    //为分配的保留支持？？？
    uint256 private unAllocatedReservedSupply;

    Locking.LockingBoxes private reservedBoxes;


    //存放分配的
    mapping (address => uint256) private allocate;


    function ReservedToken(uint256 reservedSupply_) public {
        require(reservedSupply_ > 0);
        reservedBoxes.add(reservedSupply_);
        unAllocatedReservedSupply = reservedSupply_;
    }

    function lockReserved(string name_, uint256 amount_, uint256 lockHours_) onlyOwner public {
        reservedBoxes.lock(name_, amount_, lockHours_);
    }

    function unlockReserved(string name_) onlyOwner public {
        reservedBoxes.unlock(name_);
    }

    function retrieveReserved(uint256 amount_) onlyOwner public {
        reservedBoxes.retrieve(amount_);
        freeSupply = freeSupply.add(amount_);
    }

    function allocateLockedToken(address addr_, uint256 amount_) onlyOwner public {
        require(addr_ != address(0x0));
        require(amount_ > 0 || allocate[addr_] == 0);
        require(amount_ <= unAllocatedReservedSupply);

        unAllocatedReservedSupply = unAllocatedReservedSupply.add(allocate[addr_]);
        allocate[addr_] = amount_;
        unAllocatedReservedSupply = unAllocatedReservedSupply.sub(amount_);
    }


    function getReservedSupply() view public returns (uint256, uint256, uint256) {
        uint256 unlockedReservedSupply;
        uint256 lockedReservedSupply;
        (unlockedReservedSupply, lockedReservedSupply) = reservedBoxes.getSupply();
        return (unlockedReservedSupply, lockedReservedSupply, unAllocatedReservedSupply);
    }
}


//基金会资产
contract FoundationToken is RawToken {
    using Locking for Locking.LockingBoxes;

    uint256 internal foundationSupply;

    uint256 internal bonusSupply;

    Locking.LockingBoxes private foundationBoxes;

    function FoundationToken(uint256 amount_) public {
        foundationSupply = amount_;
    }

    function applyRetrieveFoundation(string proposal_, uint256 amount_, uint256 hours_) onlyOwner public {
        //Must withdraw 15 days later
        require(hours_ >= 360);
        require(foundationSupply > amount_);
        require(amount_ > 0);

        foundationSupply.sub(amount_);
        foundationBoxes.add(amount_);
        foundationBoxes.lock(proposal_, amount_, hours_);
    }


    //取出基金会的钱
    function retrieveFoundation(uint256 amount_) onlyOwner public {
        foundationBoxes.retrieve(amount_);
        freeSupply.add(amount_);
    }
}


//分红相关
contract BonusToken is FoundationToken {

    struct AccountFaith {

    //当前代数
    uint256 currentEpoch;

    //当前最小余额
    uint256 currentMinBalance;
    }

    uint256 totalTickets = 0;

    uint256 bonusEpoch = 1;

    uint256 ticketValue = 0;


    //存放分红map
    mapping (address => AccountFaith) bonus;


    //构造函数  将amount_传给父类的构造函数
    function BonusToken(uint256 amount_) FoundationToken(amount_) public {
    }


    //分红操作
    function finalizeEpoch(uint256 amount_) onlyOwner public {
        require(foundationSupply >= amount_);
        bonusSupply = bonusSupply.add(amount_);
        foundationSupply = foundationSupply.sub(amount_);

        //0.000001
        ticketValue = bonusSupply.mul(10 ** 6).div(totalTickets);
        bonusEpoch.add(1);
    }

    function receiveBonus(address addr_) internal {
        require(addr_ != address(0x0));
        require(bonus[addr_].currentEpoch < bonusEpoch);


        uint256 bonusValue = ticketValue.mul(bonus[addr_].currentMinBalance).div(10 ** 6);
        require(bonusSupply >= bonusValue);

        balances[addr_] = balances[addr_].add(bonusValue);
        bonusSupply = bonusSupply.sub(bonusValue);
    }


    //更新分红
    function updateBonus(address addr_, uint256 value, bool from) internal {
        bool updateBalance = false;
        if (bonus[addr_].currentEpoch < bonusEpoch) {
            receiveBonus(addr_);
        }
        uint256 newMinBalance = (!from) ? balances[addr_].sub(value) : balances[addr_];
        updateBalance = updateBalance || (newMinBalance < bonus[addr_].currentMinBalance);
        if (updateBalance) {
            totalTickets = totalTickets.sub(bonus[addr_].currentMinBalance);
            totalTickets = totalTickets.add(newMinBalance);
            bonus[addr_].currentMinBalance = newMinBalance;
        }
    }

}


contract AlphaToken is BonusToken, OnSaleToken, ReservedToken, DetailedERC20 {
    using SafeMath for uint256;
    event TokenPurchase(address indexed purchaser, address indexed beneficiary, uint256 value, uint256 amount);

    // address where funds are collected
    address public wallet;

    event WalletTransferred(address indexed previousWallet, address indexed newWallet);


    function AlphaToken(address _wallet) BonusToken(10000) OnSaleToken() ReservedToken(10000) DetailedERC20("Alpha Balabala", "APP", 12) public {
        require(_wallet != address(0));

        //自由代币总数
        freeSupply = 20000;

        //总代币20000个
        totalSupply = 20000;

        //基金会钱包地址
        wallet = _wallet;
    }


    //合约交易
    function transfer(address _to, uint256 _value) public returns (bool) {
        bool success = super.transfer(_to, _value);

        //交易成功更新分红
        if (success) {
            updateBonus(msg.sender, _value, true);
            updateBonus(_to, _value, false);
        }
        return success;
    }


    //账户交易
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool) {
        bool success = super.transferFrom(_from, _to, _value);

        //更新分红
        if (success) {
            updateBonus(_from, _value, true);
            updateBonus(_to, _value, false);
        }
        return success;
    }


    //？？？？？？？？？
    // fallback function can be used to buy tokens
    function() external payable {
        if (msg.value > 0) {
            buyTokens(msg.sender);
        }
        else {
            updateBonus(msg.sender, 0, true);
        }
    }

    // low level token purchase function
    function buyTokens(address beneficiary) public canBuy payable {
        require(msg.value > 0);

        uint256 weiAmount = msg.value;
        uint256 rate = 0;
        if (state == State.Private) {
            rate == allowed[msg.sender].price;
        }
        else if (state == State.Public) {
            rate = price;
        }

        // calculate token amount to be created
        uint256 tokens = weiAmount.mul(rate);

        // update state
        remainSupply = remainSupply.sub(tokens);
        balances[beneficiary] = balances[beneficiary].add(tokens);
        if (state == State.Private) {
            assert(tokens <= allowed[msg.sender].limit);
            allowed[msg.sender].limit.sub(tokens);
        }
        updateBonus(beneficiary, tokens, false);

        TokenPurchase(msg.sender, beneficiary, weiAmount, tokens);

        forwardFunds();
    }



    //基金会 将合约地址的钱转入基金会账户
    function depositFundationToken() public onlyOwner {
        require(balances[address(this)] > 0);
        foundationSupply = foundationSupply.add(balances[address(this)]);
        balances[address(this)] = 0;
    }


    //申请取币
    function allocateToken(address _addr, uint256 _amount) public onlyOwner {
        require(_amount != 0);
        require(_addr != address(0x0));
        require(freeSupply >= _amount);

        //转过去
        freeSupply = freeSupply.sub(_amount);
        balances[_addr] = balances[_addr].add(_amount);
        updateBonus(_addr, _amount, false);
    }


    //改变钱包地址
    function changeWallet(address newWallet) public onlyOwner {
        require(newWallet != wallet);
        wallet = newWallet;
        WalletTransferred(wallet, newWallet);
    }



    function forwardFunds() internal {
        wallet.transfer(msg.value);
    }


    //返回区块数目
    function debug() view public returns (uint256) {
        return block.number;
    }

}


