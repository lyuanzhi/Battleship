package edu.duke.yl954.battleship;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.duke.yl954.battleship.vo.BasicEventData;
import edu.duke.yl954.battleship.vo.CreateRoomEventData;
import edu.duke.yl954.battleship.vo.FireEventData;
import edu.duke.yl954.battleship.vo.FireRespEventData;
import edu.duke.yl954.battleship.vo.LeaveRoomEventData;
import edu.duke.yl954.battleship.vo.MoveEventData;
import edu.duke.yl954.battleship.vo.MoveRespEventData;
import edu.duke.yl954.battleship.vo.PlaceEventData;
import edu.duke.yl954.battleship.vo.ReadyRespEventData;
import edu.duke.yl954.battleship.vo.ScanRespEventData;

import java.util.ArrayList;

@SpringBootApplication
public class WebApp implements CommandLineRunner {
    private final SocketIOServer server;
    private final Map<String, SocketIOClient> clients = new ConcurrentHashMap<>();
    private final Map<String, WebPlayer> players = new ConcurrentHashMap<>();

    public WebApp(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                String sessionId = client.getSessionId().toString();
                clients.put(sessionId, client);
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                String sessionId = client.getSessionId().toString();
                clients.remove(sessionId);
                players.remove(sessionId);
                ArrayList<CreateRoomEventData> createRoomEventDatas = new ArrayList<>();
                for (WebPlayer p : players.values()) {
                    CreateRoomEventData c = new CreateRoomEventData(p.theBoard.getWidth(), p.theBoard.getHeight(),
                            p.name, p.submarineNum, p.destroyerNum, p.battleshipNum, p.carrierNum, p.moveRemain,
                            p.scanRemain, p.myId);
                    createRoomEventDatas.add(c);
                }
                sentToAll("get_rooms_resp", CommonResult.success(createRoomEventDatas));
            }
        });
        addCreateRoomEvent();
        addGetRoomsEvent();
        addLeaveRoomEvent();
        addEnterRoomEvent();
        addPlaceEvent();
        addReadyEvent();
        addFireEvent();
        addScanEvent();
        addMoveEvent();
        server.start();
    }

    @PreDestroy
    public void stopSocketIOServer() {
        server.stop();
    }

    private void sentToAll(String eventName, Object... data) {
        server.getBroadcastOperations().sendEvent(eventName, data);
    }

    private void sentToOne(String sessionId, String eventName, Object... data) {
        clients.get(sessionId).sendEvent(eventName, data);
    }

    private void addCreateRoomEvent() {
        server.addEventListener("create_room", CreateRoomEventData.class, new DataListener<CreateRoomEventData>() {
            @Override
            public void onData(SocketIOClient client, CreateRoomEventData data, AckRequest ackSender) throws Exception {
                if (players.containsKey(data.getMyId().toString())) {
                    sentToOne(data.getMyId().toString(), "create_room_resp",
                            CommonResult.failed("Room Already Exist!"));
                    return;
                }
                Board<Character> board = new BattleShipBoard<Character>(data.getWidth(), data.getHeight(), 'X');
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                WebPlayer player = new WebPlayer(data.getPlayerName(), board, input, System.out, new V2ShipFactory(),
                        data.getSubmarineNum(), data.getDestroyerNum(), data.getBattleshipNum(), data.getCarrierNum(),
                        data.getMoveRemain(), data.getScanRemain(), data.getMyId());
                players.put(data.getMyId().toString(), player);
                sentToAll("create_room_resp", CommonResult.success(data));
            }
        });
    }

    private void addGetRoomsEvent() {
        server.addEventListener("get_rooms", BasicEventData.class, new DataListener<BasicEventData>() {
            @Override
            public void onData(SocketIOClient client, BasicEventData data, AckRequest ackSender) throws Exception {
                ArrayList<CreateRoomEventData> createRoomEventDatas = new ArrayList<>();
                for (WebPlayer p : players.values()) {
                    CreateRoomEventData c = new CreateRoomEventData(p.theBoard.getWidth(), p.theBoard.getHeight(),
                            p.name, p.submarineNum, p.destroyerNum, p.battleshipNum, p.carrierNum, p.moveRemain,
                            p.scanRemain, p.myId);
                    createRoomEventDatas.add(c);
                }
                sentToOne(data.getMyId().toString(), "get_rooms_resp", CommonResult.success(createRoomEventDatas));
            }
        });
    }

    private void addLeaveRoomEvent() {
        server.addEventListener("leave_room", LeaveRoomEventData.class, new DataListener<LeaveRoomEventData>() {
            @Override
            public void onData(SocketIOClient client, LeaveRoomEventData data, AckRequest ackSender) throws Exception {
                if (data.getHostId() != null)
                    players.remove(data.getHostId().toString());
                if (data.getMyId() != null)
                    players.remove(data.getMyId().toString());
                if (data.getEnermyId() != null)
                    players.remove(data.getEnermyId().toString());
                ArrayList<CreateRoomEventData> createRoomEventDatas = new ArrayList<>();
                for (WebPlayer p : players.values()) {
                    CreateRoomEventData c = new CreateRoomEventData(p.theBoard.getWidth(), p.theBoard.getHeight(),
                            p.name, p.submarineNum, p.destroyerNum, p.battleshipNum, p.carrierNum, p.moveRemain,
                            p.scanRemain, p.myId);
                    createRoomEventDatas.add(c);
                }
                sentToAll("get_rooms_resp", CommonResult.success(createRoomEventDatas));
                sentToOne(data.getMyId().toString(), "leave_room_resp", CommonResult.success(null));
                if (data.getEnermyId() != null) {
                    sentToOne(data.getEnermyId().toString(), "leave_room_resp", CommonResult.success(null));
                }
            }
        });
    }

    private void addEnterRoomEvent() {
        server.addEventListener("enter_room", LeaveRoomEventData.class, new DataListener<LeaveRoomEventData>() {
            @Override
            public void onData(SocketIOClient client, LeaveRoomEventData data, AckRequest ackSender) throws Exception {
                if (players.get(data.getHostId().toString()).enermy != null) {
                    sentToOne(data.getMyId().toString(), "enter_room_resp", CommonResult.failed("This Room is Full!"));
                    return;
                } else {
                    WebPlayer player = players.get(data.getHostId().toString());
                    Board<Character> board = new BattleShipBoard<Character>(player.theBoard.getWidth(),
                            player.theBoard.getHeight(), 'X');
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    player.enermy = new WebPlayer(player.name + "'s enermy", board, input, System.out,
                            new V2ShipFactory(), player.submarineNum, player.destroyerNum, player.battleshipNum,
                            player.carrierNum, player.moveRemain, player.scanRemain, data.getMyId());
                    CreateRoomEventData c = new CreateRoomEventData(player.enermy.theBoard.getWidth(),
                            player.enermy.theBoard.getHeight(),
                            player.enermy.name, player.enermy.submarineNum, player.enermy.destroyerNum,
                            player.enermy.battleshipNum, player.enermy.carrierNum, player.enermy.moveRemain,
                            player.enermy.scanRemain, player.enermy.myId);
                    sentToOne(data.getMyId().toString(), "enter_room_resp", CommonResult.success(c));
                    sentToOne(data.getEnermyId().toString(), "enter_room_resp", CommonResult.success(c));
                }
            }
        });
    }

    private void addPlaceEvent() {
        server.addEventListener("place", PlaceEventData.class, new DataListener<PlaceEventData>() {
            @Override
            public void onData(SocketIOClient client, PlaceEventData data, AckRequest ackSender) throws Exception {
                try {
                    if (data.getHostId().toString().equals(data.getMyId().toString())) {
                        players.get(data.getHostId().toString()).place(data.getShipName(), data.getRow(),
                                data.getColomn(), data.getOrientation());
                    } else {
                        players.get(data.getHostId().toString()).enermy.place(data.getShipName(), data.getRow(),
                                data.getColomn(), data.getOrientation());
                    }
                    sentToOne(data.getMyId().toString(), "place_resp", CommonResult.success(data));
                } catch (Exception e) {
                    sentToOne(data.getMyId().toString(), "place_resp", CommonResult.failed(e.getMessage()));
                }
            }
        });
    }

    private void addReadyEvent() {
        server.addEventListener("ready", LeaveRoomEventData.class, new DataListener<LeaveRoomEventData>() {
            @Override
            public void onData(SocketIOClient client, LeaveRoomEventData data, AckRequest ackSender) throws Exception {
                if (data.getHostId().toString().equals(data.getMyId().toString())) {
                    players.get(data.getHostId().toString()).isReady = true;
                } else {
                    players.get(data.getHostId().toString()).enermy.isReady = true;
                }

                if (players.get(data.getHostId().toString()).enermy != null
                        && players.get(data.getHostId().toString()).isReady == true
                        && players.get(data.getHostId().toString()).enermy.isReady == true) {
                    players.get(data.getHostId().toString()).isYourTurn = true;
                    ReadyRespEventData readyRespEventData1 = new ReadyRespEventData();
                    readyRespEventData1.setIsReady(true);
                    readyRespEventData1.setIsYourTurn(true);
                    sentToOne(data.getHostId().toString(), "ready_resp", CommonResult.success(readyRespEventData1));
                    ReadyRespEventData readyRespEventData2 = new ReadyRespEventData();
                    readyRespEventData2.setIsReady(true);
                    readyRespEventData2.setIsYourTurn(false);
                    sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "ready_resp",
                            CommonResult.success(readyRespEventData2));
                } else {
                    ReadyRespEventData readyRespEventData = new ReadyRespEventData();
                    readyRespEventData.setIsReady(false);
                    readyRespEventData.setIsYourTurn(false);
                    sentToOne(data.getMyId().toString(), "ready_resp", CommonResult.success(readyRespEventData));
                }
            }
        });
    }

    private void addFireEvent() {
        server.addEventListener("fire", FireEventData.class, new DataListener<FireEventData>() {
            @Override
            public void onData(SocketIOClient client, FireEventData data, AckRequest ackSender) throws Exception {
                String shipName = "";
                if (data.getHostId().toString().equals(data.getMyId().toString())) {
                    shipName = players.get(data.getHostId().toString()).enermy.fire(data.getRow(), data.getColomn());
                } else {
                    shipName = players.get(data.getHostId().toString()).fire(data.getRow(), data.getColomn());
                }
                players.get(data.getHostId()
                        .toString()).isYourTurn = !(players.get(data.getHostId().toString()).isYourTurn);
                players.get(data.getHostId()
                        .toString()).enermy.isYourTurn = !(players.get(data.getHostId().toString()).enermy.isYourTurn);
                FireRespEventData f1 = new FireRespEventData();
                f1.setColomn(data.getColomn());
                f1.setRow(data.getRow());
                f1.setShipName(shipName);
                f1.setIsYourTurn(players.get(data.getHostId().toString()).isYourTurn);
                sentToOne(data.getHostId().toString(), "fire_resp", CommonResult.success(f1));
                FireRespEventData f2 = new FireRespEventData();
                f2.setColomn(data.getColomn());
                f2.setRow(data.getRow());
                f2.setShipName(shipName);
                f2.setIsYourTurn(players.get(data.getHostId().toString()).enermy.isYourTurn);
                sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "fire_resp",
                        CommonResult.success(f2));
                if (players.get(data.getHostId().toString()).checkLost("")) {
                    sentToOne(data.getHostId().toString(), "gameover", CommonResult.success("Loss"));
                    sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "gameover",
                            CommonResult.success("Win"));
                }
                if (players.get(data.getHostId().toString()).enermy.checkLost("")) {
                    sentToOne(data.getHostId().toString(), "gameover", CommonResult.success("Win"));
                    sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "gameover",
                            CommonResult.success("Loss"));
                }
            }
        });
    }

    private void addScanEvent() {
        server.addEventListener("scan", FireEventData.class, new DataListener<FireEventData>() {
            @Override
            public void onData(SocketIOClient client, FireEventData data, AckRequest ackSender) throws Exception {
                players.get(data.getHostId()
                        .toString()).isYourTurn = !(players.get(data.getHostId().toString()).isYourTurn);
                players.get(data.getHostId()
                        .toString()).enermy.isYourTurn = !(players.get(data.getHostId().toString()).enermy.isYourTurn);
                int shipNum[] = null;
                if (data.getHostId().toString().equals(data.getMyId().toString())) {
                    shipNum = players.get(data.getHostId().toString()).enermy.scan(data.getRow(), data.getColomn());
                    players.get(data.getHostId().toString()).scanRemain -= 1;
                    ScanRespEventData s1 = new ScanRespEventData();
                    s1.setRow(data.getRow());
                    s1.setColomn(data.getColomn());
                    s1.setScanSN(shipNum[0]);
                    s1.setScanDN(shipNum[1]);
                    s1.setScanBN(shipNum[2]);
                    s1.setScanCN(shipNum[3]);
                    s1.setScanRemain(players.get(data.getHostId().toString()).scanRemain);
                    s1.setIsYourTurn(players.get(data.getHostId().toString()).isYourTurn);
                    sentToOne(data.getHostId().toString(), "scan_resp", CommonResult.success(s1));
                    ScanRespEventData s2 = new ScanRespEventData();
                    s2.setIsYourTurn(players.get(data.getHostId().toString()).enermy.isYourTurn);
                    sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "scan_resp",
                            CommonResult.success(s2));
                } else {
                    shipNum = players.get(data.getHostId().toString()).scan(data.getRow(), data.getColomn());
                    players.get(data.getHostId().toString()).enermy.scanRemain -= 1;
                    ScanRespEventData s1 = new ScanRespEventData();
                    s1.setRow(data.getRow());
                    s1.setColomn(data.getColomn());
                    s1.setScanSN(shipNum[0]);
                    s1.setScanDN(shipNum[1]);
                    s1.setScanBN(shipNum[2]);
                    s1.setScanCN(shipNum[3]);
                    s1.setScanRemain(players.get(data.getHostId().toString()).enermy.scanRemain);
                    s1.setIsYourTurn(players.get(data.getHostId().toString()).enermy.isYourTurn);
                    sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "scan_resp",
                            CommonResult.success(s1));
                    ScanRespEventData s2 = new ScanRespEventData();
                    s2.setIsYourTurn(players.get(data.getHostId().toString()).isYourTurn);
                    sentToOne(data.getHostId().toString(), "scan_resp", CommonResult.success(s2));
                }

            }
        });
    }

    private void addMoveEvent() {
        server.addEventListener("move", MoveEventData.class, new DataListener<MoveEventData>() {
            @Override
            public void onData(SocketIOClient client, MoveEventData data, AckRequest ackSender) throws Exception {
                try {
                    MoveRespEventData m1 = null;
                    if (data.getHostId().toString().equals(data.getMyId().toString())) {
                        m1 = players.get(data.getHostId().toString()).move(data.getOldRow(), data.getOldColomn(),
                                data.getNewRow(), data.getNewColomn(), data.getNewOrientation(), data.getIndex());
                    } else {
                        m1 = players.get(data.getHostId().toString()).enermy.move(data.getOldRow(), data.getOldColomn(),
                                data.getNewRow(), data.getNewColomn(), data.getNewOrientation(), data.getIndex());
                    }
                    players.get(data.getHostId()
                            .toString()).isYourTurn = !(players.get(data.getHostId().toString()).isYourTurn);
                    players.get(data.getHostId().toString()).enermy.isYourTurn = !(players
                            .get(data.getHostId().toString()).enermy.isYourTurn);
                    MoveRespEventData m2 = new MoveRespEventData();
                    if (data.getHostId().toString().equals(data.getMyId().toString())) {
                        m1.setIsYourTurn(players.get(data.getHostId().toString()).isYourTurn);
                        m2.setIsYourTurn(players.get(data.getHostId().toString()).enermy.isYourTurn);
                        sentToOne(players.get(data.getHostId().toString()).enermy.myId.toString(), "move_resp",
                                CommonResult.success(m2));
                    } else {
                        m1.setIsYourTurn(players.get(data.getHostId().toString()).enermy.isYourTurn);
                        m2.setIsYourTurn(players.get(data.getHostId().toString()).isYourTurn);
                        sentToOne(data.getHostId().toString(), "move_resp", CommonResult.success(m2));
                    }
                    sentToOne(data.getMyId().toString(), "move_resp", CommonResult.success(m1));
                } catch (Exception e) {
                    sentToOne(data.getMyId().toString(), "move_resp", CommonResult.failed(e.getMessage()));
                }
            }
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
