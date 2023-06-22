```sql
CREATE TABLE `Products` (
  `Sku` varchar(100) NOT NULL,
  PRIMARY KEY (`Sku`)
);
```

```sql
CREATE TABLE `Batches` (
  `Reference` varchar(255) NOT NULL,
  `Sku` varchar(255) NOT NULL,
  `PurchasedQuantity` int NOT NULL,
  `Eta` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`Reference`),
  KEY `Batches_FK` (`Sku`),
  CONSTRAINT `Batches_FK` FOREIGN KEY (`Sku`) REFERENCES `Products` (`Sku`) ON DELETE CASCADE
);
```

```sql
CREATE TABLE `OrderLines` (
  `OrderId` varchar(255) NOT NULL,
  `Sku` varchar(255) NOT NULL,
  `Quantity` int NOT NULL,
  `BatchReference` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`OrderId`,`Quantity`,`Sku`),
  KEY `IX_OrderLines_Reference` (`BatchReference`),
  CONSTRAINT `FK_OrderLines_Batches_Reference` FOREIGN KEY (`BatchReference`) REFERENCES `Batches` (`Reference`) ON DELETE CASCADE
);
```